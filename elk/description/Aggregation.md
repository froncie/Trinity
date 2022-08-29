# Elasticsearch : Aggregation

---

## Aggregation
**Aggregation** in Elasticsearch is the ability to group data and get aggregates.

Request for aggregation - Response form
```http
GET {INDEX}/_search
{
	"aggs":{
		"my_aggs":{
			"agg_type":{
				...
			}
		}
	}
}
```

aggs → Request for Aggregation 
my_aggs → the Name of Aggregation.
agg_type → the Type of Aggregation, There are Two types, 
→ ***Metric Aggregations*** and ***Bucket Aggregations***
❗️ Metric Aggregations for Statistics or Calculations.
❗️ Bucket Aggregations for Grouping Documents.

Response Form
```http
{
	...
	"hits":{
		"total":{
			...
		}
	},
	"aggregations":{
		"my_aggs":{
			%% Value is Result of Aggregations%%
			"value":
		}
	}
}
```

### ⚡️ Metric Aggregations
Metric Aggregations represent Result of such Min/Max/Sum/Avg/Median. 

| Metric       | Description                                 |
| ------------ | ------------------------------------------- |
| avg          | Average                                     |
| min          | Minimum                                     |
| max          | Maximum                                     |
| sum          | Sum                                         |
| percentiles  | Percentage                                  |
| status       | min + max + sum + count(Count of Documents) |
| cardinality  | Count of Unique Values                      |
| geo-centroid | Calculate the center point of the location information inside the field

```http
%% AVG or Percentiles %%
GET {INDEX}/_search
{
	%% SIZE 0 doesn't include the document used for Aggregation. → save resources. %%
	%% Set 'size=0' to not show search hits because only want to see aggregated results in the response.%%
	"size":0,
	"aggs":{
		"avg_aggs":{
			"avg":{
				"field":"FIELD_NAME"
			}
		},
		"per_aggs":{
			"percentiles":{
				"field":"FIELD_NAME"
				"percents":[
					%% One is in 25%, the another is in 50% %%
					25,
					50
				]
			}
		}
	}

}
```

##### ✨ Cardinality Aggregations use HyperLogLog++ Algorithm
```http
GET {INDEX}/_search
{
	"size":0,
	"aggs":{
		"card_aggs":{
			"cardinality":{
				"field":"day_of_week",
				
				%% Accuracy figures, Hight Value, High resources needed and accuracy %%
				"precision_threshold":100
			}
		}
	}
}
```

##### ✨ Aggregations in Results.
Search → Aggregations
```http
GET {INDEX}/_search
{
	"size":0,
	"query":{
		"term":{
			"FIELD_for_QUERY":"FIELD_VALUE"
		}
	},
	"aggs":{
		"query_aggs":{
			"sum":{
				"FIELD_for_AGGS": "FIELD_NAME"
			}
		}
	}
}
```

### ⚡️ Bucket Aggregations
It is used for Groupping Documents. 

| Bucket             | Description                                                                                                                     |
| ------------------ | ------------------------------------------------------------------------------------------------------------------------------- |
| histogram          | Sort numeric type fields at regular intervals                                                                                   |
| date_histogram     | Sort Date/Time type fieldsat regulat intervals                                                                                  |
| range              | Sort numeric type fields into user-specified intervals                                                                          |
| date_range         | Sort date/time type fields into user-specified intervals                                                                        |
| terms              | Sort based on terms that appear frequently in the field                                                                         |
| siginificant_terms | Classification based on statistically significant values in the current search condition compared to all documents in the index |
| filters            | Manually specify the conditions of documents to be included in each group                                                       |

✨ Histogram Aggregation
Histogram Aggregations is Sorting numeric type fields at regular intervals.

```http
GET {INDEX}/_search
{
	"size":0,
	"aggs":{
		"histogram_aggs":{
			"histogram":{
				"field":"FIELD_NAME",

				%% the Reqular interval is set to 100, 0~99, 100~199, ... %%
				"interval": 100
			}
		}
	}
}
```

✨ Range Aggregation
Using User-speicified Range 

```http
GET {INDEX}/_search
{
	"size":0,
	"aggs":{
		"range_aggs":{
			"range":{
				"field":"FIELD_NAME",
				"ranges":{
					{"from": 0, "to": 50},
					{"from": 50, "to": 80},
					{"from": 80, "to": 100}
				}
			}
		}
	}
}
```

✨ Term Aggregation
Sorting based on terms that appear frequently in the field

```http
GET {INDEX}/_search
{
	"size":0,
	"aggs":{
		"term_aggs":{
			"terms":{
				"field": "day_of_week

				%% Output only top 6 %%
				"size": 6
			}
		}
	}
}
```

✨ Increase Term Aggregations Accuracy
If the size is smaller than the number of values a field can have, an error may occur in the aggregation.

```http
GET {INDEX}/_search
{
	"size":0,
	"aggs": {
		"term_aggs":{
			"terms":{
				"field": "day_of_week",
				"size":6,
				"show_term_doc_count_error":true
				%% [1] "shard_size": PROPER_SHARD_SIZE %%
			}
		}
	}
}

%% RESULT %%
{
...
	"term_aggs":{
		"doc_count_error_upper_bound": 0,
		"sum_other_doc_count" : ...,
		"buckets": [
			{
				"key" : "KEY",
				"doc_count": COUNT,
				"doc_count_error_upper_bound" : 0
				%% → There is no error. %%
				%% → If there are some error, have to increase the size of shard parameter → [1] on the first comment. %%
			}
		]
	}
}
```

###### ⚡️  Combination of Aggregations
✨  Metric Aggregations + Bucket Aggregations
Following example is applied to Field for terms → Field for AVG 

```http
GET {INDEX}/_search
{
	"size":0,
	"aggs":{
		"term_aggs":{
			"terms":{
				"field": "FIELD for terms",
				"size":5
			}
		},
		"aggs":{
			"avg_aggs":{
				"avg":{
					"field": "FIELD for AVG"
				}
			}
		}
	}
}
```

✨ TERM → AVG & SUM 

```http
GET {INDEX}/_search
{
	"size":0,
	"aggs":{
		"term_aggs":{
			"term":{
				"field": "FIELD for TERM AGGS",
				"size": 5
			},
			"aggs":{
				"avg_aggs":{
					"avg":{
						"field": "FIELD for AVG"
					}
				},
				"sum_aggs":{
					"sum":{
						"field": "FIELD for SUM"
					}
				}
			}
		}
	}
}
```

✨ Sub Bucket Aggregations
Bucket Aggregation in Bucket Aggregation.

Histogram → Term Aggregations by TOP 2. 

```http
GET {INDEX}/_search
{
	"size":0,
	"aggs":{
		"histogram_aggs":{
			"histogram":{
				"field": "FIELD for Histogram",
				"interval": 100
			},
			"aggs":{
				"term_aggs":{
					"term":{
						"field": "FIELD for term",
						"size": 2
					}
				}
			}
		}
	}
}
```

##### ⚡️  Pipeline Aggregation
Aggregation method borrowing the concept of a pipeline that uses the previous result in the next step.

✨ Parent Aggregation
```http
{
	"aggs":{
		...
		"aggs":{
			...
		}
	}
}
```

✨ Sibling Aggregation
```http
{
	"aggs":{
		
	},
	"aggs":{
	}
}
```

✨ cumulative sum w/ Parent Aggregation
Histogram ⊂ cum_sum
```http
GET {INDEX}/_search
{
	"size":0,
	"aggs":{
		"histogram_aggs":{
			"histogram":{
				"field": "FIELD for Histogram",
				"interval": INTERVAL
			},
			"aggs":{
				"sum_aggs":{
					"sum":{
						"field": "FIELD for SUM"
					}
				}
			},
			"cum_sum":{
				"cumulative_sum":{
					"buckets_path":"sum_aggs"
				}
			}
		}
	}
}
```

✨ Sum w/ Sibling Aggregation
term an sum_total
```http
GET {INDEX}/_search
{
	"size":0,
	"aggs": {
		"term_aggs":{
			"term":{
				"field": "FIELD for term",
				"size": SIZE
			},
			"aggs":{
				"sum_aggs":{
					"sum":{
						"field": "FIELD for SUM"
					}
				}
			}
		},
		"sum_total":{
			"sum_bucket":{
				%% > symbol indicates sub-aggregation %%
				"buckets_path":"term_aggs>sum_aggs"
			}
		}
	}
}
```

---
