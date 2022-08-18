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
It is used to set regular or meaningful values such as categories, people, brands, etc.
The keyword type does not go through the parser and the entire string is indexed as a single term.