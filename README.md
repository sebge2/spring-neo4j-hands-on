# Spring Neo4J

The purpose of this project is to play with Neo4J integrated with Spring. This project
is a sample Know Your Customer Application for Service Provider Company in computing.


## Installation

````
docker pull neo4j
````

````
docker run \
    --publish=7474:7474 --publish=7687:7687 \
    --volume=$HOME/neo4j/data:/data \
    neo4j
````

Go on [local page](http://localhost:7474/). Credentials: neo4j/neo4j.


## Example Queries

Find all company names:

````
MATCH (company:Company) RETURN company.name AS name
````

## Links

* [Docker Hub](https://hub.docker.com/_/neo4j)
* [Spring Neo4J](https://spring.io/guides/gs/accessing-data-neo4j)
* [Spring Neo4J](https://docs.spring.io/spring-data/neo4j/docs/7.0.0/reference/html/)