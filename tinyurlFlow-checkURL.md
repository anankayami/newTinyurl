```mermaid
sequenceDiagram
    title URL Validation Sequence Diagram with Frontend and Backend Checks

    participant User
    participant Frontend as Frontend
    participant Nginx
    participant Backend as Backend Service
    participant Validator as URL Validator Service

    User ->> Frontend: Enter URL
    alt URL is empty or exceeds 3000 characters
        Frontend -->> User: Display "URL cannot be empty or exceed 3000 characters"
    else Valid URL input
        Frontend ->> Nginx: POST /api/shorten {originalUrl: "http://example.com"}
        Nginx ->> Backend: POST /api/shorten {originalUrl: "http://example.com"}
        Backend ->> Validator: Validate URL
        alt Valid URL
            Validator -->> Backend: Valid URL response
            Backend -->> Nginx: Valid URL response
            Nginx -->> Frontend: Valid URL response
            Frontend -->> User: Display "URL is valid and shortened successfully"
        else Invalid URL
            Validator -->> Backend: Invalid URL response
            Backend -->> Nginx: Invalid URL response
            Nginx -->> Frontend: Invalid URL response
            Frontend -->> User: Display "URL is invalid"
        end
    end