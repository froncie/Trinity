# Logstash
---

### Getting Started 
```bash
%% Ubuntu v22.04 %%
apt install openjdk-11-jdk
```

✨ Logstash, Input is stdin and Ouput is stdout
```bash
docker run -d --name logstash --rm -v {LOCAL_CONFIG_FOLDER}:{CONTAINER_CONFIG_FOLDER} -it docker.elastic.co/logstash/logstash:8.2.2 -e "input { stdin { } } output { stdout {  }}"
```

Pipeline configuration is required to run Logstash.
Typically, the pipeline configuration is written in **/usr/share/logstash/config/piplelines.yml**.

| Log Level | Description                                              |
| --------- | -------------------------------------------------------- |
| fatal     | Errors severe enough to cause the system to stop working |
| error     | Does not stop system operation, but causes errors        |
| warn      | Warning include potential errors                         |
| info      | To know information such as progress or status changes   |
| debug     | For debugging in developing progress                     |
| trace     | To trace system progress                                                         |

##### ✨ Pipeline
Pipeline is the most important in Logstash. 
Data → ***PIPELINE*** → Elasticsearch 

```conf
input {
	{ ... }
}

filter {
	{ ... }
}

output {
	{ ... }
}
```

There are various types of input plugins in Logstash.

| Input Plugin | Description                             |
| ------------ | --------------------------------------- |
| file         | Like linux tail -f                      |
| syslog       | Receive Syslog transfer through network |
| kafka        | Read Data from Kafka topic              |
| jdbc         | Executes a query with the jdbc driver at a specified schedule and reads the result.                                        |

PIPELINE 파일을 지정하는 옵션
```bash
logstash -f {PIPELINE_FILE}
```

✨ Filter
Logstash filter formalizes unstructured data and forms a structure for data analysis.

| common options | Description                               |
| -------------- | ----------------------------------------- |
| add_field      | Add new field                             |
| add_tag        | Add new tag to successful event           |
| enable_metric  | Used to analyze the performance of filter |
| id             | Set plugin ID                             |
| remove_field   | Remove Field                              |
| remove_tag     | Remove tag from succesful event                                          |

Frequently used filters

| Filter  | Description                                                                                                       |
| ------- | ----------------------------------------------------------------------------------------------------------------- |
| grok    | Analyze the message in a structured form.                                                                         |
| dissect | Although the degree of freedom is lower than that of grok, it can be processed quickly by using a simple pattern. |
| mutate  | Provides general processing functions such as changing field names and processing strings                         |
| date    | Parse a string into the date format of the specified pattern.                                                                                                                  |

For example
sincedb_path is the position where to start reading from the last. 
in this case, It's nul, it means it isn't set yet.
after first read, it will be set to the last position 
sincedb_path's default Dir is stored into data/plugins/inputs/file. 
```log
%% filter-example.log %%
[2020-01-02 14:17] [ID1] 192.10.2.6 8080 [INFO] - connected.
[2020/01/02 14:19:25]   [ID2] 218.25.32.70 443 [warn] - busy server.
```
```pipeline
input {
	file{
		path=>"{FILTER_DIR}/filter-example.log"
		start_position => "beginning"
		sincedb_path => "nul"
	}
}

output {
	stdout{}
}
```

✨ mutate, Split string
Mutate can rename, change or delete fields, etc.

| mutate    | Description                                                             |
| --------- | ----------------------------------------------------------------------- |
| split     | Split a string with a delimiter.                                        |
| rename    | rename fields.                                                          |
| replace   | Change value of a field to specified value                              |
| uppercase | Change letters to uppercase                                             |
| lowercase | ... lowercase                                                           |
| join      | Concatenate into a single string by concatenating them with delimiters. |
| gusb      | Replaces matches of the regular expression with another string.         |
| merge     | Include one field in another field.                                     |
| coerce    | Defaults to null field values.                                          |
| strip     | Remove left and right whitespace from field values.                                                                        |

**Options are applied in this order.**
coerce - rename - update - replace - convert - gsub - uppercase - capitalize - lowercase - strip - remove - split - join - merge - copy
```logstash
input {
	file {
		path => "{FILTER_FILE_DIR}/.log"
		start_position => "beginning"
		sincedb_path => "nul"
	}
}

filter {
	mutate {
		split => { "message" => " " }
	}
}

output {
	stdout { }
}
```

remove field and add field
```
...
filter {
	split => { "message" => " "}

	%% Print the second of the message field as id %%
	add_field => { "id" => "%{[message][2]}"}
	remove_field => "message"
}
```

Change Case
```pipeline
...
filter {
	dissect {
		... [%{level}]
	}
	mutate {
		uppercase => ["level"]
	}
}
```

DATE/TIME string analyze
```pipeline
...
filter {
	dissect {
		... [%{timestamp}]
	}
	mutate {
		strip => "timestamp"
	}
	date {
		match => [ "timestamp", "YYYY-MM-dd HH:mm", "yyyy/MM/dd HH:mm:ss"]
		target => "new+timestamp"
		timezone => "UTC"
	}
}
```

✨ dissect, Parse strings 
IF the log has a foam is \[timestamp\] \[id\] ip port \[level\] - msg, You can parse the information by mapping as follows.
```pipeline
...
filter {
	dissect {
		mapping => {"message"=> "[%{timestamp}] [%{id}] %{ip} %{port} [%{level}] - %{message}."}
	}
}
...
```

An error may occur when the delimiter is not constant, such as when the number of white spaces varies.
```pipeline
...
filter {
	dissect {
		mapping => {"message"=> "[%{timestamp}]%{?->}[%{id}] %{ip} %{+ip} [%{level}] - %{message}."}
	}
}
```
***%{?->}*** Ignores whitespace after making it a field.
ip means merge with ip field.

✨ grok, Parse strings

| grok pattern      | Description                                                                               |
| ----------------- | ----------------------------------------------------------------------------------------- |
| NUMBER            | Decimal, sign and decimal point.                                                          |
| SPACE             | Recognize spaces.                                                                         |
| URI               | Recognize URI. It include Protocol, Authenciation information, Host, path, parameter.     |
| IP                | Recognize IP(IPv4, IPv6)                                                                  |
| SYSLOGBASE        | Recognizes the header part of the syslog.                                                 |
| TIMESTAMP_ISO8601 | Recognizes a timestamp in the form of recording accurate information up to the time zone. |
| DATA              | Used when special values ​​do not need to be verified.                                    |
| GREEDYDATA        | If it is located at the end of the expression, the value from that position to the end of the event is recognized. Subsequent data is data that does not require verification                                                                                          |

✨ Split with grok
```pipeline
...
filter {
	grok {
		match => {"message"=> "\[%{TIMESTAMP_ISO8601:timestamp}\] [ ]*\[%{DATA:id}\] %{IP:ip} %{NUMBER:port:int} \[%{LOGLEVEL:level}\] \- %{DATA:message}\."}
	}
}
...
```

IF you want to change the format of TIMESTAMP
```pipeline
...
filter {
	grok {
		pattern_definitions => {"MY_TIMESTAMP" => "%{YEAR}[/-]%{MONTHNUM}[/-]%{MONTHDAY}[T ]%{HOUR}:?%{MINUTE}(?::?%{SECOND})?%{ISO8601_TIMEZONE}?"}
		... "%{MY_TIMESTAMP:timestamp}"
	}
}
```

**💡 regular pattern → consider to use DISSECT** 
💡 if not → grok 

---

✨ Statement
if, else if, else
```pipeline
...
filter {
	dissect {
		...
	}
	if [level] == "INFO" {
		drop { }
	}
	else if [level] == "warn" {
		mutate {
			remove_field => ["ip", "port", "timestamp", "level"]
		}
	}
}
```

##### ⚡️  OUTPUT
Pipeline : Input → Filter → **OUTPUT**
```pipeline
input {
	file {
		path => " ... "
		start_position => "beginning"
		sincedb_path => "nul"
	}
}

filter {
	...
}

output {
	file {
		path => "PATH to be stored."
	}
	elasticsearch {
		%% hosts, index, document_id, user/password, pipeline, template/template_name %%
		index => "output"
	}
}
```

##### ⚡️ codec
Plugins used in input and output processes.

input codec
```pipeline
input {
	file {
		path => " ... "
		start_position => "beginning"
		sincedb_path => "nul"
		codec => "Codec format according to extension format"
	}
}

output {
	stdout { }
}
```

⚡️ Multiple Pipeline 
Pipeline 1 : beats(input) → dissect → elasticsearch(output)
Pipeline 2 : kafka(input) → grok → elasticsearch(output)
```pipeline
input {
	beats{... tag => beats}
	kafka{... tag => kafka}
}

filter {
	if "beats" in [tags]
	{
		dissect { ... }
	} else if "kafka" in [tags] {
		grok { ... }
	}
}

output {
	if "beats" in [tags] {
		elasticsearch {
			...
		}
	} else if "kafka" in [tags] {
		file {
			path => " ... "
		}
	}
}
```

✨ How to run multiple pipelines
configuration of pipelines.yml
```yml
- pipelineid: pipeline1
  path.config: "pipeline1 config file"
- pipelineid: pipeline2
  path.config: "pipeline2 config file"
```
| config              | description                                                                                     |
| ------------------- | ----------------------------------------------------------------------------------------------- |
| pipeline.id         | Unique ID of pipeline                                                                           |
| path.config         | Path of the config                                                                              |
| pipeline.workers    | Number of workers to process filter and output in parallel                                      |
| pipeline.batch.size | Determines the maximum number of events to be processed at the same time per worker upon input. |
| queue.type          | Queue type, Default is memory. another type is persisted if you want to reduce the usage of resource.                                                                                                |

##### ⚡️ Monitoring
There are several monitoring methods.
There are two main methods.
One is using Logstash API.
The another one is Kibana.

✨ logstash API
```bash
curl -XGET "localhost:9600?pretty"
```

✨ Kibana Monitoring 
Configurate the config file **(logstash/config/logstash.yml)**
And in Kibana, Management > Stack Monitoring > "Or, set up with self monitoring" > Turn on monitoring 