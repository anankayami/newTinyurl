package com.tinyurl.demo.service;

import com.tinyurl.demo.mapper.UrlMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tinyurl.demo.model.UrlTable;
import java.security.SecureRandom;
import java.util.List;

@Service
public class UrlService {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private static final int PREGENERATED_URL_COUNT = 40000;        //The actual number to be prepared is 14,400,000,000
    private static final int PRELOAD_URL_COUNT = 10000; 

    @Autowired
    private UrlMapper urlMapper;

    // Preload data into the pre_reload_urls table
    @Scheduled(cron = "0 0 0 * * ?")
    public void preloadShortUrls() {
        int existingCount = urlMapper.countPreReloadUrls();
        int remainingCount = PRELOAD_URL_COUNT - existingCount;
        if (remainingCount > 0) {
            List<String> shortUrls = urlMapper.getRandomShortUrlsFromPreGenerated(remainingCount);
            for (String shortUrl : shortUrls) {
                urlMapper.preloadShortUrlToReload(shortUrl);
            }
            urlMapper.deleteShortUrlsFromPreGenerated(shortUrls); // Delete the extracted short URL
        }
    }

    // Pre-generate short URLs and store them in the database
    public void preGenerateShortUrls() {
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < PREGENERATED_URL_COUNT; i++) {
            String shortUrl = generateRandomShortUrl(random);
            urlMapper.insertPreGeneratedUrl(shortUrl);
        }
    }

    // Generate a random short URL
    private String generateRandomShortUrl(SecureRandom random) {
        StringBuilder sb = new StringBuilder(SHORT_URL_LENGTH);
        String shortUrl;
        do {
            sb.setLength(0);    // reset StringBuilder
            for (int i = 0; i < SHORT_URL_LENGTH; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            shortUrl = sb.toString();
        } while (urlMapper.existsShortUrl(shortUrl));   // Check for uniqueness
        return shortUrl;
    }

    // Shorten the original URL and return the short URL
    public String shortenUrl(String originalUrl) {
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
;
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String returnUrl = baseUrl + "/api/url/" + shortUrl;
        return returnUrl;
    }

    // Retrieve the original URL based on the short URL
    public String getOriginalUrl(String shortUrl) {
        UrlTable urlTable = urlMapper.getUrlByShortUrl(shortUrl);
        if (urlTable != null) {
            urlMapper.incrementClickCount(shortUrl);
        }
        return urlMapper.findOriginalUrlByShortUrl(shortUrl);
    }

    // Retrieve the click count for the short URL
    public int getClickCount(String shortUrl) {
        return urlMapper.getClickCountByShortUrl(shortUrl);
    }

    // Scheduled task to delete data older than 2 years every midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledDeleteOldUrls() {
        List<UrlTable> getOldUrlsList = urlMapper.getOldUrls();

        for (UrlTable url : getOldUrlsList) {
            // Process each URL as needed
            urlMapper.insertOldUrlToPreGenerated(url.getShortUrl());
        }
        
        urlMapper.deleteOldUrls();
    }
}
