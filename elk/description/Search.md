# Elasticsearch : Search
---

## Elasticsearch : Search 
### Query Context
The **Query context** calculates the similarity to the query and shows more accurate results based on this. Returns similarity score according to the result
```http
GET kibana_sample_data_ecommerce/_search
{
	"query":{
		"match":{
			"category":"clothing"
		}
	}
}
```

### Query String
The query string is a way to write a query statement to the URI address of the REST API. it is like Requesting a query in the form of parameters.
```http 
GET kibana_sample_data_ecommerce/_search?q=customer_full_name:Mary
```

### Query DSL
Query DSL writes a query in JSON format in the request body of the REST API.
Query DSL is more powerful than Query String, but if you use a simple query, it would be better to use Query string.
```http
GET kibana_sample_data_ecommerce/_search
{
	"query":{
		"match":{
			"customer_full_name":"Mary"
		}
	}
}
```

---
 
### Filter Context 
the **Filter context** does not calculate similarity and returns only results according to ***whether or not they match***.
**FIlter Context** doesn't calculate similarity score and no need to perform updates every time so that is available to use cache. if you need to search quickly, You may consider using Filter context.

Filter Context is applied to the filter type inside the logical query.
The following example is a request to find documents where the field "day_of_week" is "Friday".
```http
GET kibana_sample_data_ecommerce/_search
{
	"query":{
		"bool":{
			"filter":{
				"term":{
					"day_of_week":"Friday"
				}
			}
		}
	}
}
```

### Similarity Score
Elasticsearch use BM25 algorithm to calculate similarity score.
**Similarity Score** is a value expressing the similarity between a query statement and a document.
High score, High similarity.
```http
GET kibana_sample_data_ecommerce/_search
{
	"query":{
		"match":{
			"products.product_name":"Pants"
		}
	},
	"explain": true
}
```
You can find documents that contain *products.product_name* is Pants.
***explain: true*** tells you how to optimize the query internal, which path was searched for, and what criteria the score was calculated based on.

### BM25(Score Algorithm)
If you look at the response value requested by the query context, all hit documents have score values.

❗**IDF (Inverse Document Frequency)**
An indicator of how often a particular term appears
Then, Why IDF is important? 
→ Articles and conjunctions, Adverbs may not be important in sentences.
→ The smaller the occurrence frequency in the document, the higher the weight is called **IDF(Inverse Document Frequency)**.
→ "idf, computed as log( 1 + ( N - n + 0.5) / ( n + 0.5 )) from:"
```http
{
	"value":7.1974354,
	"description": "idf, computed as log( 1 + ( N - n + 0.5) / ( n + 0.5 )) from:",
	"details":[
		{
			"value":3,
			"description": "n, number of documents containing term",
			"details":[]
		},
		{
			"value":4675,
			"description": "N, total number of docuemnts with field",
			"details":[]
		}
	]
},
...
```

❗️ **TF(Term Frequency)**
An indicator of how many times a particular term appears in ***A document***.
If a particular term is repeated a lot in the document, it is more likely that the term is related to the subject of the document.
```http
{
	"value" : 0.52217203,
	"description":"tf, computed as freq / ( freq + k1 * ( 1 - b + b * dl / avgdl)) from:",
	"details":[
		{
			"value":1.0,
			"description": "freq, occurences of term within document",
			"details":[]
		},
		{
			"value":1.2,
			"description":"k1, term saturation parameter",
			"details":[]
		},
		{
			"value":0.75,
			"description":"b, length normalization parameter",
			"details":[]
		},
		{
			"value":5.0,
			"description":"dl, length of field",
			"details":[]
		},
		{
			"value":7.3161497,
			"description":"avgdl, average length of field",
			"details":[]
		}
	]

}
```

❗️ **score(freq=1.0), computed as boost * idf * tf from : **
in this, boost is 2.2 in Elasticsearch 
