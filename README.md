# CCB developer-test-server - Egleston

To build, do one of the following:
```
 mvn clean package
   -- OR --
 docker run -ti --rm -v $(pwd):/code -w /code maven mvn clean package
```
>
> Add `-v $(pwd)/.m2:/root/.m2` if planning to build more than once

This will generate a WAR file under the `target/` directory. To run:
 `docker-compose up --build`

This will start two docker containers:
 - db  - a vanilla MySql database populated with the Sakila Video Store database
 - web - an Apache/Tomcat servlet engine with the WAR file (from above) responding to all URLs starting with `/movies/`
> Note: I decided to have this extra layer of URL handling since there might be other servlets running nearby.
>
>       I could install the WAR-file as ROOT servlet and modify web.xml to map `/movies` instead of `/api`


The following URLs are available:
  - `/movies/`                                   - simple HTML page with links to demonstrate functionality
  - `/movies/api/film`                           - all films (accepts optional query-string parameters)
    - `title`    - defaults to all
    - `rating`   - defaults to all
    - `category` - defaults to all
    - `orderBy`  - defaults to `title`
    - `limit`    - defaults to all
    - `offset`   - defaults to zero (0)
  - `/movies/api/detail/####`                    - a single film identified by the `film_id`
  - `/movies/api/pretty`                         - toggles pretty-printing of JSON (no feedback - redirects back automatically)

TODO:
 - JUnit tests
 - SQL Injection checks (sqlmap)
 - logging integration (log4j2 with file rotation)
 - proper MySql configuration (properties file, external datastore, etc.)
 - connection pooling, exception handling, retry/reconnect
 - full DAO/CRUD table-to-object mappings

