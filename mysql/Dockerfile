FROM mysql

ENV MYSQL_ROOT_PASSWORD     secret
ENV MYSQL_DATABASE          sakila
ENV MYSQL_USER              sakila
ENV MYSQL_PASSWORD          sakila

COPY data/sakila-schema.sql /docker-entrypoint-initdb.d/1-schema.sql
COPY data/sakila-data.sql   /docker-entrypoint-initdb.d/2-data.sql

CMD [ "mysqld", "--default-authentication-plugin=mysql_native_password" ]
