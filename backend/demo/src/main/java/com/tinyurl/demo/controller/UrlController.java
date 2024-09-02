package com.tinyurl.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tinyurl.demo.service.UrlService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/")
    public String index() {
        return "Shortener URL";
    }

    // Endpoint to pre-generate short URLs and store them in the database
    @PostMapping("/preGenerate")
    public void preGenerateShortUrls() {
        urlService.preGenerateShortUrls();
    }

    // Endpoint to pre-reload short URLs and store them in the database
    @PostMapping("/preReload")
    public void preReloadShortUrls() {
        urlService.preloadShortUrls();
    }

    // Endpoint to delete data older than 2 years
    @PostMapping("/deleteOldUrls")
    public void deleteOldUrls() {
        urlService.scheduledDeleteOldUrls();
    }

    // Index endpoint to check if the service is running
    @PostMapping("/shorten")
    public ResponseEntity<Map<String, Object>> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("originalUrl");
        if (!isValidUrl(originalUrl)) {
            return buildErrorResponse("Invalid URL format", "The provided URL format is not valid.", HttpStatus.OK);
        }

        String shortUrl = redisTemplate.opsForValue().get(originalUrl);
        if (shortUrl == null) {
            try {
                shortUrl = urlService.shortenUrl(originalUrl);
            } catch (Exception e) {
                return buildErrorResponse("Shortening Error", "An error occurred while shortening the URL.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            shortUrl = baseUrl + "/api/" + shortUrl;
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("originalUrl", originalUrl, "shortUrl", shortUrl));
        response.put("message", "URL has been shortened successfully");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to redirect to the original URL based on the short URL
    @GetMapping("/{shortUrl}")
    public void getConOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        try {
            String originalUrl = urlService.getOriginalUrl(shortUrl);
            if (originalUrl != null) {
                response.sendRedirect(originalUrl);
            } else {
                String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                response.sendRedirect(baseUrl);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found", e);
        }
    }

    // Endpoint to get the click count of a short URL
    @GetMapping("/{shortUrl}/clicks")
    public ResponseEntity<Integer> getClickCount(@PathVariable String shortUrl) {
        try {
            int clickCount = urlService.getClickCount(shortUrl);
            return new ResponseEntity<>(clickCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to validate the URL format
    private boolean isValidUrl(String url) {
        try {
            // Parse the URL
            URI uri = new URI(url);
            
            // Check if the scheme and host are present
            if (uri.getScheme() == null || uri.getHost() == null) {
                return false;
            }
            
            // Check if the scheme is http or https
            String scheme = uri.getScheme().toLowerCase();
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                return false;
            }
            
            // Create a URL object to further validate
            URL parsedUrl = uri.toURL();
            
            // Check if the URL is well-formed
            parsedUrl.toURI();
            
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
    // Build a standard error response
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, String description, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        response.put("error", Map.of("code", status.value(), "description", description));

        return new ResponseEntity<>(response, status);
    }

    // Global exception handler for ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse("Resource Not Found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Custom exception for not found resources
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
