# CCB developer-test-server - Egleston

To build, do one of the following:
```
 mvn clean package
 docker run -ti --rm -v $(pwd):/code -w /code maven mvn clean package
```

This will generate a WAR file under the target/ directory. To run:
 `docker-compose up --build`

This will start two docker containers:
 - db  - a vanilla MySql database populated with the Sakila Video Store database
 - web - an Apache/Tomcat servlet engine with the WAR file from above mounted at the /movies/

The following URLs are available:
  - `/movies/webapi/film`                           - all films
    * optional arguments (query-string)
    - `title`   - defaults to all
    - `rating`  - defaults to all
    - `orderBy` - defaults to `title`
    - `offset`  - defaults to zero (0)
    - `limit'   - defaults to all
  - `/movies/webapi/detail/####`                    - a single film identified by the `film_id`

  * The user should be able to filter the movies by category
* A list of actors in a movie



TODO:
 - logging integration (log4j2 with file rotation)
 - proper MySql configuration (properties file, external datastore, etc.)
 - connection pooling, exception handling, retry/reconnect
 - full DAO/CRUD table-to-object mappings
 - JUnit tests
 - SQL Injection checks (sqlmap)

