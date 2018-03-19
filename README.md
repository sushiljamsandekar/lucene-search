# lucene-search
## Spring Boot + Rest Service + Lucene Search + File Search

## Rest Services:

1. **getMatchingFiles** -> 

PARAM 1: keywords - Mandatory, Type - String
PARAM 2: isFuzzy - Optional, Type - boolean

Provide the keywords to search. Can be separated by space or comma. If exact match for all words is needed, keep the fuzzy flag with its default value. In case you need to search files containing any word from the given list, set the flag to TRUE. JSON will be returned with desired data.

2. **buildIndexForDirectory** -> 

PARAM 1: path - Mandatory, Type - String

Specify the directory to be indexed. Program will traverse the directory till its end child and perform the indexing. As of now only text files are supported. Index operation is performed in its separate threadpool.

## Technology Stack
1. Java 8
2. Spring Boot
3. Spring MVC
4. Lucene Core
