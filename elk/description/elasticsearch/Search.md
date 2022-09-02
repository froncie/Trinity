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

---

### Query 
Elasticsearch supports Query divided into **Leaf Query** and **Compound Query** to search.

##### ⚡ **Full Text Query**
Fields for ***full-text search*** should be mapped to text type when mapping indexes. (:= Googling)
```http
PUT qIndex/_doc/1
{
	%% Elastic is Uppercase %%
	"contents":"I love Elastic search"
}
```
```http
GET qIndex/_search
{
	"match":{
		%% elastic is lowercase%%
		"contents":"elastic search"
	}
}

%% RESULT %%
But Its result is MATCH !
```

##### ⚡️  Match Query
Match query is typical One of Full-text query.
If you want to use Match query, you should know the field name.
and Don't know → Use \_mapping API
```http
GET {INDEX_NAME}/_mapping
```

```http
GET kibana_sample_data_ecommerce/_search
{
	%% _source shows us certain field. %%
	"_source":["cutomer_full_name"],
	"query":{
		"match":{
			"customer_full_name":"Mary"
		}
	}
}
```

Operator change
Default is 'OR'
```http
GET kibana_sample_data_ecommerce/_search
{
	%% _source shows us certain field. %%
	"_source":["cutomer_full_name"],
	"query":{
		"match":{
			"customer_full_name":{
				"query":"Mary",
				"operator": "and"
			}
		}
	}
}
```

##### ⚡️ Match Phrase Query 
Used to search for terms that contain two or more words.
```http
GET kibana_sample_data_ecommerce/_search
{
	%% _source shows us certain field. %%
	"_source":["cutomer_full_name"],
	"query":{
		"match_phrase":{
			"customer_full_name":"Mary"
		}
	}
}
```

##### ⚡️ Multi Match Query
When we want to search something in multiple fields
```http
GET kibana_sample_data_ecommerce/_search?explain=true
{
	%% _source shows us certain field. %%
	"_source":["cutomer_first_name", "customer_last_name","customer_full_name"],
	"query":{
		"multi_match":{
			"query":"mary",
			"match":[
				"customer_first_name",
				"customer_last_name",
				"customer_full_name"
			]
		}
	}
}
```
And can Use Wild Card
```http
GET kibana_sample_data_ecommerce/_search?explain=true
{
	%% _source shows us certain field. %%
	"_source":["cutomer_first_name", "customer_last_name","customer_full_name"],
	"query":{
		"multi_match":{
			"query":"mary",
			"fields":"customer_*_name"
		}
	}
}
```

##### ⚡️  Boosting
How to weight a specific field among multiple fields. 
```http
GET elastic_blog/_search
{
	"query":{
		"multi_match":{
			"query":"Elastic",
			"fields":[
				%% ^2 is boosted!  %%
				"title^2",
				"body",
				"footer"
			]
		}
	}
}
```

---

##### ⚡️  Term-level Query
Term-level queries, on the other hand, are used ***to find exact matches*** and must be mapped to keyword types.
```http
PUT qIndex/_doc/1
{
	%% Tech Uppercase%%
	"category": "Tech"
}
```
```http
GET qIndex/_search
{
	"term":{
		%% tech lowercase %%
		"category":"tech"
	}
}

%% RESULT %%
tech in Search Query and document are different Case.
It doesn't match ! 
```
❗️ In general, It is used to search Number, Date, Categorical data. 
It is similar with Where clause in RDB.
There are Term Query, Terms Query, Fuzzy Query in Term-Level Query. 

##### ⚡️ Term Query 
Term Query is typical One of Term-Level Query. 
It is CASE SENSITIVE because of Term-Level Query and it is mapped to Keyword type( It does not go through the analyzer. )

```http
GET kibana_sample_data_ecommerce/_search
{
	%% _source shows us certain field. %%
	"_source":["cutomer_full_name"],
	"query":{
		"term":{
			"customer_full_name":"Mary"
		}
	}
}
```

✨ If The field is Multi Field contains Text type and Keyword, You can consider to use Full-text query
```http
GET kibana_sample_data_ecommerce/_search
{
	%% _source shows us certain field. %%
	"_source":["cutomer_full_name"],
	"query":{
		"term":{
			%% customer_full_name => TEXT TYPE%%
			%% customer_full_name.keyword => keyword type %%
			%% Search exact "Mary Bailey" in documents %%
			"customer_full_name.keyword":"Mary Bailey"
		}
	}
}
```

💡 If you use Match Query, The fields should be mapped to Text type.
💡 If you use Term Query, The fields should be mapped to Keyword type.

##### ⚡️ Terms Query
```http
GET kibana_sample_data_ecommerce/_search
{
	%% _source shows us certain field. %%
	"_source":["day_of_week"],
	"query":{
		"terms":{
			%% Search Field has Monday or Friday %%
			"day_of_week":["Monday","Friday"]
		}
	}
}
```

---

##### ⚡️  Range Query
Used to search for data contained within a range by specifying a range.
```http
GET kibana_sample_data_flights/_search
{
	"query":{
		"range":{
			"timestamp":{
				%% 2020-12-15 00:00:00 ~ 2020-12-15 23:56:59 %%
				"gte":"2020-12-15",
				"lt":"2020-12-16"
			}
		}
	}
}
```

✨  current base expression
```http
GET kibana_sample_data_flights/_search
{
	"query":{
		"range":{
			"timestamp":{
				%% from one month to now %%
				"gte":"now-1M"
			}
		}
	}
}
```
| Time | Meaning |     | Time | Meaning |     | Time | Meaning |
| ---- | ------- | --- | ---- | ------- | --- | ---- | ------- |
| y    | year    |     | M    | Month   |     | w    | weeks   |
| d    | days    |     | H,h  | Hours   |     | m    | minutes |
| s    | seconds        |     |      |         |     |      |         |

✨  Relation parameter
```http
GET kibana_sample_data_flights/_search
{
	"query":{
		"range":{
			"timestamp":{
				%% from one month to now %%
				"gte":"now-1M"
				"relation":"within"
			}
		}
	}
}
```
| Range                                      | Relation   |
| ------------------------------------------ | ---------- |
| Some of Document Data in Query Range       | intersects |
| All of Document Range Data in Query Range  | within     |
| All of Query Range in Document Range  Data | contains   |

##### ⚡ Logical query
Logical Query is One of Compound Queries. 
It is used to search for the ones whose IP is '192.168.0.1' among the logs created on '2021-01-21'.
Logical Query supports 4 type for compound query.
```http
GET {INDEX}/_search
{
	"query":{
		"bool":{
			"must":[
				%% AND operated. %%
				{"term":{"day_of_week":"Sunday"}},
				{"match":{"elastic_blog_title":"search"}}
			],
			"must_not
				%% Not include elastic_blog_body is CRUD %%
				"term":{"elastic_blog_body":"CRUD"}
			],
			"should":[
				%% OR operated. %%
				%% IF this is used with MUST, Weight to result of "should" %%
				{"term":{"day_of_week":"Sunday"}},
				{"match":{"elastic_blog_title":"search"}}
			],
			"filter":[
				%% It doesn't calculate Score so that filters unnecessary documents %%
				%% Below, find documents with elastic_blog_page from 10 to 45 and apply MUST. %%
				"range":{
					"elastic_blog_page":{
						"gte":10,
						"lte":45
					}
				}
			]
		}
	}
}
```

##### ⚡️ Pattern Search
There are cases when the search term you want to search for is long or you do not know the exact search term.
There are two types in Pattern Search, WildCard and Regexp Query. 

✨  **Wild card Query**
Wildcards can be used with two symbols, * and ?, to search for terms.
"\*" can match any character regardless of the number of characters.
"?" cna match only one character. 
But, If you use them at the beginning of the term, Performance can be slow.
```http
GET {INDEX}/_search
{
	"_source": "Field",
	"query":{
		"wildcard":{
			"field.keyword":"V?l*" %% := V(a)l(ue) %%
		}
	}
}
```

**✨ Regexp Query (Regular Expression Query)**
Regexp Query can be used with REGEXP in Query.
```http
GET {INDEX}/_search
{
	"query":{
		"regexp":{
			"field":"Val.." %% Val(ue)%%
		}
	}
}
```

💡 Basically, in order to improve search efficiency, the query context and filter context should be well combined, but if you want to speed up the query, you should actively use the filter context.

--- 

