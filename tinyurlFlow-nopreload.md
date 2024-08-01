```mermaid
sequenceDiagram
    title URL Shortener Sequence Diagram with Redis

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
        Backend1 ->> SQLite: INSERT INTO urls (original_url, short_url) VALUES ("http://example.com", "abc123")
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
    end
    Backend2 ->> SQLite: UPDATE urls SET click_count = click_count + 1 WHERE short_url = "abc123"
    SQLite -->> Backend2: OK
    Backend2 -->> Nginx: 302 Found {Location: "http://example.com"}
    Nginx -->> User: 302 Found {Location: "http://example.com"}