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
eg.
CONTAINER ID   IMAGE                 COMMAND                  CREATED              STATUS              PORTS                    NAMES
1e7331c2e676   newtinyurl_frontend   "/docker-entrypoint.…"   About a minute ago   Up About a minute   0.0.0.0:8082->80/tcp     newtinyurl_frontend_1
94fa1f2a6fb5   newtinyurl_backend1   "java -jar demo.jar …"   About a minute ago   Up About a minute   0.0.0.0:8080->8080/tcp   newtinyurl_backend1_1
c1714147aaf0   newtinyurl_backend2   "java -jar demo.jar …"   About a minute ago   Up About a minute   0.0.0.0:8081->8081/tcp   newtinyurl_backend2_1
5b2856ccf3b3   redis:alpine          "docker-entrypoint.s…"   About a minute ago   Up About a minute   0.0.0.0:6379->6379/tcp   newtinyurl_redis-server_1

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
#####   4.2 login SQLite
```
     cd db
     sqlite3 database.tinyurl
```
#####   4.3 eixt SQLite
```
    .exit
```
#####   4.4 logout container
```
    exit
```

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



