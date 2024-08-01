```mermaid
sequenceDiagram
    title URL Shortener Sequence Diagram with Redis and Preload Table

    participant User
    participant Nginx
    participant Backend1 as Backend Service 1
    participant Backend2 as Backend Service 2
    participant Redis as Redis Cache
    participant SQLite as SQLite Database

    User ->> Nginx: POST /api/shorten {originalUrl: "http://example.com"}
    Nginx ->> Backend1: POST /api/shorten {originalUrl: "http://example.com"}
    Backend1 ->> Redis: GET originalUrl
    alt Cache miss
        Redis -->> Backend1: (cache miss)
        Backend1 ->> SQLite: SELECT COUNT(*) FROM pre_reload_urls
        alt No preloaded URLs
            SQLite -->> Backend1: 0
            Backend1 ->> SQLite: SELECT short_url FROM pre_generated_urls LIMIT 10000
            SQLite -->> Backend1: shortUrls
            Backend1 ->> SQLite: INSERT INTO pre_reload_urls (short_url) VALUES (shortUrls)
            Backend1 ->> SQLite: DELETE FROM pre_generated_urls WHERE short_url IN (shortUrls)
        else Preloaded URLs exist
            SQLite -->> Backend1: count
        end
        Backend1 ->> SQLite: SELECT short_url FROM pre_reload_urls ORDER BY RANDOM() LIMIT 1
        SQLite -->> Backend1: shortUrl
        Backend1 ->> SQLite: INSERT INTO urls (original_url, short_url) VALUES ("http://example.com", shortUrl)
        Backend1 ->> SQLite: DELETE FROM pre_reload_urls WHERE short_url = shortUrl
        SQLite -->> Backend1: OK
        Backend1 ->> Redis: SET originalUrl=shortUrl, shortUrl=originalUrl
        Redis -->> Backend1: OK
    end
    Backend1 -->> Nginx: 200 OK {shortUrl: "http://short.ly/api/abc123"}
    Nginx -->> User: 200 OK {shortUrl: "http://short.ly/api/abc123"}

    User ->> Nginx: GET /api/abc123
    Nginx ->> Backend2: GET /api/abc123
    Backend2 ->> Redis: GET shortUrl
    alt Cache hit
        Redis -->> Backend2: originalUrl (cache hit)
    else Cache miss
        Backend2 -->> SQLite: SELECT original_url FROM urls WHERE short_url = abc123
        SQLite -->> Backend2: OK
        Backend2 -->> Redis: SET originalUrl=shortUrl, shortUrl=originalUrl
    end
    Backend2 ->> SQLite: UPDATE urls SET click_count = click_count + 1 WHERE short_url = "abc123"
    SQLite -->> Backend2: OK
    Backend2 -->> Nginx: 302 Found {Location: "http://example.com"}
    Nginx -->> User: 302 Found {Location: "http://example.com"}