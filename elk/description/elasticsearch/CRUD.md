# CRUD(Create, Read, Update, Delete)
### PUT method - CREATE Index 
```bash
curl --cacert http_ca.crt -u elastic -XPUT sample_index https://{YOUR IP}:9200/
```

### GET method - READ Index 
Using Document ID
```bash
curl --cacert http_ca.crt -u elastic -XGET https://{YOUR_IP}:9200/sample_index/_doc/1 
```

Using DSL(Domain Specific Language), SEARCH all in a Index
``` bash
curl --cacert http_ca.crt -u elastic -XGET https://{YOUR_IP}:9200/sample_index/_search 
```

### Bulk for One Request 
Bulk API Request, Bulk can be processed by one time 
Only can request CREATE, UPDATE, DELETE not READ(GET)
```http
POST _bulk
{"index":{"_index:"test","_id":"1"}}
{"field1" : "value1"}
{"create":{"_index":"test","_id":"3"}}
{"field1" : "value3"}
{"update":{"_index":"test","_id":"1"}}
{"doc":{"field2" : "value2"}}
{"delete":{"_index":"test","_id":"2"}}
```
```http
POST _bulk
{"index":{"_index":"index2","_id":"4"}}
{"name":"park","age":30,"gender":"female"}
{"index":{"_index":"index2","_id":"5"}}
{"name":"jung","age":50,"gender":"male"}
```
```bash
curl --cacert http_ca.crt -H "Content-Type: application/x-ndjson" -XPOST https://localhost:9200/_bulk --data-binary 
@./bulk_index2 -u elastic
```

### Mapping 
##### 1. Dynamic Mapping
All indexes in Elasticsearch have mapping information, but for flexible use, mapping definitions are not enforced when creating indexes.

#### 2. Explicit Mapping
Defining index mappings directly
```http
PUT "INDEX"
{
	"mappings":{
		"properties":{
			"field1":{"type":"_TYPE_"},
			...
		}
	}
}
```
```http
PUT index3
{
	"mappings":{
		"properties":{
			"age":{"type":"short"},
			"name":{"type":"text"},
			"gender":{"type":"keyword"}			
		}
	}
}
```

### Inverted Indexing with Text type
In general, a string containing a sentence or multiple words is specified as the text type.
The strings are tokenized and each token is indexed 
IT IS ***Inverted Indexing***


### Search using DSL query
```http
GET _search
{
  "query": {
    "match_all": {}
  }
}

%% Create Index %%
PUT text_index
{
  "mappings": {
    "properties": {
      "contents": {"type": "text"}
    }
  }
}

```

- ***match*** is a DSL query that can perform a whole sentence search 
- Find the document if it contains any of the inverted indexed terms
- Elasticsearch **doesn't support** aggregation or sorting for the Text type
- Although aggregation or sorting can be supported as a mapping parameter, it has the disadvantage of using a lot of memory.
- so if you want to aggregate or sort, You should use **Keyword type** for it not **Text type** 
```http
%% Create a Doc in the index Id is 1 %%
PUT text_index/_doc/1
{
  "contents": "beautiful day"
}

%% Search Doc has string contains "day" in the index %%
GET text_index/_search
{
    "query": {
	    "match": {
		    "contents": "day"}
		}
	}
}
```
### Keyword Type 
- It is used to set regular or meaningful values such as categories, people, brands, etc.
- The keyword type does not go through the parser and the entire string is indexed as a single term.

Create Keyword Type Index
```http
PUT keyword_index
{
	"mappings":{
		"properties": {
			"Field_Name":{"type":"keyword"}
		}
	}
}
```
```http
PUT keyword_index/_doc/1
{
	"contents(Field_Name)":"beautify day"
}
```

### Multi Field Index 
- Multifield is the ability to define multiple subfields for a single field input with a keyword is **" fields "**
- Use in case your data grows and requires a full-text search.
```http
PUT multifield_index
{
	"mappings":{
		"properties":{
			"message":{"type":"text"},
			"contents":{
				"type":"text",
				"fields":{
					"keyword":{"type":"keyword"}
				}
			}
		}
	}
}
```
- You can use multi-field to search the contents of a field with a keyword type while having a text type.
```http
PUT multifield_index/_doc/1
{
  "message": "1 document",
  "contents": "beautiful day"
}

PUT multifield_index/_doc/2
{
  "message": "2 document",
  "contents": "beautiful day"
}

PUT multifield_index/_doc/3
{
  "message": "3 document",
  "contents": "wonderful day"
}
```
- Search for Text Type 
```http
GET multifield_index/_search
{
  "query": {
    "match": {
      "contents": "day"
    }
  }
}
```
- Search for Keyword Type
```http
GET multifield_index/_search
{
	"query":{
		"term":{
			"contents.keyword":"day"
		}
	}
}
```

- Aggregation for the index 
```http
GET multifield_index/_search
{
	"size":0,
	"aggs":{
		"contents":{
			"terms":{
				"field":"contents.keyword"
			}
		}
	}
}
```

### Index Template
- Used to create indexes with the same settings

GET template
```http
GET _index_template

GET _index_template/ilm-history
```

Create Template
```http
PUT _index_template/test_template
{
	"index_patterns":["test_*"],
	"priority":1,
	"template":{
		"settings":{
			"number_of_shards": 3,
			"number_of_replicas": 1
		},
		"mappings":{
			"properties":{
				"name":{"type":"text"},
				"age":{"type":"short"},
				"gender":{"type":"keyword"}
			}
		}
	}
}
```

- Template Priority
It is set to the template has higher priority
in this case, multi_template2 is set to multi_data_index because of priority 
```http
PUT _index_template/multi_template1
{
	"index_patterns":"multi_*",
	"priority":1,
	"template":{
		"mappings":{
			"properties":{
				"age":{"type":"integer"},
				"name":{"type":"text"}
			}
		}
	}
}

PUT _index_template/multi_template2
{
	"index_patterns":"multi_data_*",
	"priority":2,
	"template":{
		"mappings":{
			"properties":{
				"age":{"type":"integer"},
				"name":{"type":"text"}
			}
		}
	}
}

PUT multi_data_index
GET multi_data_index/_mapping
```

### Dynamic Template 
When indexing **log systems** or **unstructured data**, it is used when it is difficult to accurately define the field type and the number of fields cannot be determined.
```http
PUT dynamic_index1
{
	"mappings":{
		"dynamic_templates":[
		  {
			  "my_string_fields":{
				  "match_mapping_type":"string",
				  "mapping":{"type":"keyword"}
			  }
		  }
		]
	}
}
```
A document is added to "dynamic_index"
```http
PUT dynamic_index1/_doc/1
{
  "name":"Mr. Kim",
  "age":40
}
```
You can see Mapping information of the dynamic_index1
```http
GET dynamic_index1/_mapping

%% Mapping information %%
{
  "dynamic_index1" : {
    "mappings" : {
      "dynamic_templates" : [
        {
          "my_string_fields" : {
            "match_mapping_type" : "string",
            "mapping" : {
              "type" : "keyword"
            }
          }
        }
      ],
      "properties" : {
        "age" : {
          "type" : "long"
        },
        "name" : {
          "type" : "keyword"
        }
      }
    }
  }
}
```

#### Dynamic mapping 2 
```http
PUT dynamic_index2
{
	"mappings":{
		"dynamic_templates":[
		{
			"my_long_fields":{
				"match":"long_*",
				"unmatch":"*_text",
				"mapping":{"type":"long"}
			}
		}]
	}
}
```
```http
PUT dynamic_index2/_doc/1
{
	"long_num":"5",
	"long_text":"170"
}
```

#### Dynamic Template Statement
| Statement                | Description                                                                    |
| ------------------------ | ------------------------------------------------------------------------------ |
| match_mapping_type       | Check the type of data and change some of them to the mapping type pre-defined |
| match, unmatch           | If the pattern matches, if it does not match                                   |
| match_pattern            | Adjust the parameters available in the match pattern. e.g) REGEX               |
| path_match, path_unmatch | it is used to the pattern include ' **.** ' like path                                                                               |
<br>

### Analyzer
- Elasticsearch uses inverted indexing to support full-text searches
- Full-text search is performing partial search in long strings 
- Inverted Indexing is indexing technology to analyze long strings and split them into smaller units. 
So it is important to set Good Standard to split strings into smaller units.
Elasticsearch has **Character Filter**, **Tokenizer**, and **Token Filter**
Cute Dog → Character Filter(Cute Dog) → Tokenizer(Cute, Dog) → Token Filter(cute, dog) 

| Term |
| ---- |
| cute |
| dog  |

### Inverted Indexing
inverted Indexing is like the <mark class="hltr-green">INDEX</mark> on the back of the book.

### Analyzer API 
There are some Analyzer in Elasticsearch. 
in this, you have to know Standard, Stop, Simple, Whitespace Analyzer
- Stop Analyzer
```http 
POST _analyze
{
	"analyzer":"stop",
	"text":"The 10 most loving dog breeds"
}

%% RESULT IN %%
{
	"tokens":[
		{"token":"most",...},
		{"token":"loving",...},
		{"token":"dog",...},
		{"token":"breeds",...}
	]
}
```

| Analyzer   | Description                                                                                     |
| ---------- | ----------------------------------------------------------------------------------------------- |
| standard   | Default Analyzer in Elasticsearch. it include Standard Tokenizer, Lowercase Filter, Stop Filter |
| simple     | Only letters will be tokenized                                                                  |
| whitespace | Tokens are separated by spaces.                                                                 |
| stop       | it is similar with simple analyzer but include stop filter so the word 'the' is deleted by stop filter                                                                                                |

### Tokenizer
The tokenizer separates the string and tokenizes it. it must be included in Analyzer so we have to choose proper Tokenizer.

| Tokenizer     | Description                                                                                          |
| ------------- | ---------------------------------------------------------------------------------------------------- |
| standard      | standard analyzer uses this tokenizer. this delete '.' or ',' in strings and tokenize based on text. |
| lowercase     | this tokenize based on text, All letters of strings be changed to lower case                         |
| ngram         | Tokenization by dividing into N units                                                                                                     |
| uax_url_email |it is to tokenize url or email                                                                    |

- uax_url_email tokenizer
```http
POST _analyze
{
	"tokenizer":"uax_url_email",
	"text":"email: elastic@elk-company.com"
}

%% result %%
{
	"tokens":[
		{"token":"email",...},
		{"token":"elastic@elk.company.com",...}
	]
}
```

### Filter 
Analzyer combines one tokenizer and multiple filters.
Filter is needed One Tokenizer
```http
POST _analyze
{
	"tokenizer":"standard",
	"filter":["uppercase"],
	"text": "The 10 most loving dog breeds."
}

%% RESULT, All Letters in the string is changed to Uppercase %%
{
	"tokens":[
		{"token":"THE",...},
		{"token":"10",...},
		{"token":"MOST",...},
		{"token":"LOVING",...},
		{"token":"DOG",...},
		{"token":"BREEDS",...}
	]
}
```

### Character Filter
The character filter is placed before the tokenizer and serves to preprocess the characters.
for example, it will be comportable that perform to change **&nbsp;** to whitespace in ***Character Filter***

### Token Filter
Token Filter applies a filter to the characters tokenized by the tokenizer.
it can be possible to modify, add, delete to Tokens.

| Filter    | Description                                                          |
| --------- | -------------------------------------------------------------------- |
| lowercase | Converts all characters to lowercase.                                |
| stemmer   | it is for analyzing English grammer                                  |
| stop      | This can remove certain words that the default filter cannot remove. | 

### Custom Analyzer
**Custom analyzer** is an analyzer that users can use by directly combining tokenizers and filters when there is no analyzer that satisfies the desired function among the built-in analyzers provided by Elasticsearch.

- Create Custom Analyzer
```http
PUT custom_analyzer
{
	"settings":{
		"analysis":{
			"filter":{
				"my_stopwords":{
					"type":"stop",
					"stopwords":["lions"]
				}
			},
			"analyzer":{
				"my_analyzer":{
					"type":"custom",
					"char_filter":[],
					"tokenizer":"standard",
					"filter":["lowercase","my_stopwords"]
				}
			}
		}
	}
}
```
- TEST the Custom Analyzer
```http
GET custom_analyzer/_analyze
{
	"analyzer":"my_analyzer",
	"text":"Cats Lions Dogs"
}

%% RESULT %%
{
	"tokens":{
		{"token":"cats",...},
		{"token":"dogs",...}
	}
}
```

**The order of the filters is important to get the expected result.**
Expected results may not be obtained in the following order.
```http
GET custom_analyzer/_analyze
{
	"tokenizer":"standard",
	"filter":["my_stopwords","lowercase"],
	"text":"Cats Lions Dogs"
}

%% RESULT %%
{
	"tokens":{
		{"token":"cats"},
		{"token":"lions"},
		{"token":"dogs"}
	}
}
```