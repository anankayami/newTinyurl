package com.tinyurl.demo.service;

import com.tinyurl.demo.mapper.UrlMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tinyurl.demo.model.UrlTable;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

@Service
public class UrlService {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private static final int PREGENERATED_URL_COUNT = 40000;        //The actual number to be prepared is 14,400,000,000
    private static final int PRELOAD_URL_COUNT = 10000; 

    @Autowired
    private UrlMapper urlMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisLockService redisLockService;

    // Preload data into the pre_reload_urls table
    @Scheduled(cron = "0 0 0 * * ?")
    public void preloadShortUrls() {
        System.out.println("preloadShortUrls executed at: " + LocalDateTime.now());

        String lockKey = "preloadShortUrlsLock";
        String lockValue = UUID.randomUUID().toString();

        boolean acquired = redisLockService.acquireLock(lockKey, lockValue, 600); // lock retain 600s

        if (!acquired) {
            // If the lock cannot be acquired, skip below the process.
            return;
        }
        try {
            int existingCount = urlMapper.countPreReloadUrls();
            int remainingCount = PRELOAD_URL_COUNT - existingCount;
            if (remainingCount > 0) {
                List<String> shortUrls = urlMapper.getRandomShortUrlsFromPreGenerated(remainingCount);
                for (String shortUrl : shortUrls) {
                    urlMapper.preloadShortUrlToReload(shortUrl);
                }
                urlMapper.deleteShortUrlsFromPreGenerated(shortUrls); // Delete the extracted short URL
            }
        } catch (Exception e) {
            throw new UrlServiceException("Failed to preload short URLs", e);
        } finally {
            redisLockService.releaseLock(lockKey, lockValue);
        }
    }

    // Pre-generate short URLs and store them in the database
    public void preGenerateShortUrls() {
        SecureRandom random = new SecureRandom();
        try {
            for (int i = 0; i < PREGENERATED_URL_COUNT; i++) {
                String shortUrl = generateRandomShortUrl(random);
                urlMapper.insertPreGeneratedUrl(shortUrl);
            }
        } catch (Exception e) {
            throw new UrlServiceException("Failed to pre-generate short URLs", e);
        }
    }

    // Generate a random short URL
    private String generateRandomShortUrl(SecureRandom random) {
        StringBuilder sb = new StringBuilder(SHORT_URL_LENGTH);
        String shortUrl;
        try {
            do {
                sb.setLength(0);    // reset StringBuilder
                for (int i = 0; i < SHORT_URL_LENGTH; i++) {
                    sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
                }
                shortUrl = sb.toString();
            } while (urlMapper.existsShortUrl(shortUrl));   // Check for uniqueness
        } catch (Exception e) {
            throw new UrlServiceException("Error generating random short URL", e);
        }
        return shortUrl;
    }

    // Shorten the original URL and return the short URL
    @Transactional
    public String shortenUrl(String originalUrl) {
        String lockKey = "lock:shortenUrl:" + originalUrl;
        String lockValue = UUID.randomUUID().toString();

        boolean acquired = redisLockService.acquireLock(lockKey, lockValue, 60); // lock retain 60s

        if (!acquired) {
            // If the lock cannot be acquired, throw error
            throw new IllegalStateException("Failed to acquire lock for shortening URL");
        }
        try {
            // check pre_reload_urls count
            int existingCount = urlMapper.countPreReloadUrls();
            if (existingCount == 0) {
                List<String> shortUrls = urlMapper.getRandomShortUrlsFromPreGenerated(PRELOAD_URL_COUNT);
                for (String shortUrl : shortUrls) {
                    urlMapper.preloadShortUrlToReload(shortUrl);
                }
                urlMapper.deleteShortUrlsFromPreGenerated(shortUrls); // Delete the extracted short URL
            }
            String shortUrl = urlMapper.getRandomShortUrlFromReload();
            UrlTable url = new UrlTable();
            url.setOriginalUrl(originalUrl);
            url.setShortUrl(shortUrl);
            url.setClickCount(0);
            urlMapper.insertUrl(url);
            urlMapper.deleteShortUrlFromReload(shortUrl);
            // save Redis cache
            redisTemplate.opsForValue().set(shortUrl, originalUrl, 2, TimeUnit.DAYS);
            redisTemplate.opsForValue().set(originalUrl, shortUrl, 2, TimeUnit.DAYS);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String returnUrl = baseUrl + "/api/" + shortUrl;
            return returnUrl;
        } catch (Exception e) {
            throw new UrlServiceException("Failed to shorten URL", e);
        } finally {
            redisLockService.releaseLock(lockKey, lockValue);
        }
    }

    // Retrieve the original URL based on the short URL
    public String getOriginalUrl(String shortUrl) {
        try {
            // get data from Redis cache
            String originalUrl = redisTemplate.opsForValue().get(shortUrl);
            if (originalUrl == null) {
                originalUrl = urlMapper.findOriginalUrlByShortUrl(shortUrl);
                if (originalUrl != null) {
                    // save Redis cache
                    redisTemplate.opsForValue().set(shortUrl, originalUrl, 2, TimeUnit.DAYS);
                    redisTemplate.opsForValue().set(originalUrl, shortUrl, 2, TimeUnit.DAYS);
                }
            }
            if (originalUrl != null) {
                urlMapper.incrementClickCount(shortUrl);
            }
            return originalUrl;
        } catch (Exception e) {
            throw new UrlServiceException("Failed to retrieve original URL", e);
        }
    }

    // Retrieve the click count for the short URL
    public int getClickCount(String shortUrl) {
        try {
            return urlMapper.getClickCountByShortUrl(shortUrl);
        } catch (Exception e) {
            throw new UrlServiceException("Failed to get click count for URL", e);
        }
    }

    // Scheduled task to delete data older than 2 years every midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledDeleteOldUrls() {
        System.out.println("scheduledDeleteOldUrls executed at: " + LocalDateTime.now());

        String lockKey = "scheduledDeleteOldUrlsLock";
        String lockValue = UUID.randomUUID().toString();

        boolean acquired = redisLockService.acquireLock(lockKey, lockValue, 600); // lock retain 600s

        if (!acquired) {
            // If the lock cannot be acquired, skip below the process.
            return;
        }
        try {
            List<UrlTable> getOldUrlsList = urlMapper.getOldUrls();

            for (UrlTable url : getOldUrlsList) {
                // Process each URL as needed
                urlMapper.insertOldUrlToPreGenerated(url.getShortUrl());
            }
            
            urlMapper.deleteOldUrls();
        } catch (Exception e) {
            throw new UrlServiceException("Failed to delete old URLs", e);
        } finally {
            redisLockService.releaseLock(lockKey, lockValue);
        }
    }
    // Custom exception for UrlService errors
    public static class UrlServiceException extends RuntimeException {
        public UrlServiceException(String message) {
            super(message);
        }

        public UrlServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
