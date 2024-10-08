 ## Shortener URL
This is a URL Shortener application built using Quasar for the frontend and Spring Boot for the backend. The application uses SQLite as the database.
###   Requisites
######      ・Java 
######      ・JavaScript
######      ・Spring boot
######      ・Mybatis
######      ・Maven
######      ・Quasar
######      ・Vue
######      ・SQLite
######      ・Redis
######      ・Nginx

### Getting start
####    1.clone project 
```
git clone https://github.com/anankayami/newTinyurl.git
```

####    2.start server
```
docker-compose up --build
```
###### confirm container
```
docker ps -a
```
### eg.
```
NAMES
newtinyurl_frontend_1
newtinyurl_backend1_1
newtinyurl_backend2_1
newtinyurl_redis-server_1
newtinyurl_nginx_1
```

###### stop container
```
docker-compose down 
```

#### The backend server should start at http://localhost:8080 and http://localhost:8081.

####  The frontend should be available at http://localhost:8082.

---------------------------------------------------------------------------------

####    4.Database
###### The application uses SQLite as the database. The database file is located at backend/demo/db/database.tinyurl
#####   4.1 login container
```
docker ps
docker exec -it newtinyurl_backend1_1 sh
```
#####  4.2 login SQLite
```
cd db
sqlite3 database.tinyurl
```
####  4.3 create table
```hash
DROP TABLE IF EXISTS pre_generated_urls;

CREATE TABLE pre_generated_urls (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    short_url VARCHAR(6) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS pre_reload_urls;

CREATE TABLE pre_reload_urls (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    short_url VARCHAR(6) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    original_url TEXT NOT NULL,
    short_url VARCHAR(6) NOT NULL UNIQUE,
    click_count INTEGER DEFAULT 0,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 4.4  create data of table pre_generated_urls (just one time)
``` bash
curl -X POST "http://localhost:8080/api/preGenerate" -H "Content-Type: application/json"
```

#### 4.5 import data into pre_reload_urls from pre_generated_urls
```bash
curl -X POST "http://localhost:8080/api/preReload" -H "Content-Type: application/json"
```
####  4.6  search data
```bash
  1. login database terminal
      sqlite>

  2. create table

  3. confirm pre_generated_urls data count
      sqlite> select count(*) from pre_generated_urls;

  4. confirm pre_reload_urls data count
      sqlite> select count(*) from pre_reload_urls;

  5. confirm urls data count
      sqlite> select count(*) from urls;
      sqlite> select * from urls;
```
#### 4.7 confirm data
#####  view shortURL click count(${shortUrl} is parameter)
```bash
curl -X GET "http://localhost:8080/api/${shortUrl}/clicks"
curl -X GET "http://localhost:8081/api/${shortUrl}/clicks" 
```

#####   4.8 eixt SQLite
```
.exit
```
#####   4.9 logout container
```
exit
```
-----------------------------------------------

#### 5. redis
#####   5.1 login container
```
docker ps
docker exec -it demo_redis-server_1 sh
```
#####   5.2 login Redis
```
redis-cli
```
#####   5.3 get Redis data
```
get <key>
```
###### eg. get 「original URl」  or get 「shortUrl」

#####   5.4 show all Redis data
```
KEYS *
```
#####   5.5  logout redis
```
exit
```
#####   5.6  logout constainer
```
exit
```

------------------------------------------------------------



