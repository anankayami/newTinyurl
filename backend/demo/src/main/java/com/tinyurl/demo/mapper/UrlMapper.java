package com.tinyurl.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tinyurl.demo.model.UrlTable;

@Mapper
public interface UrlMapper {
    // Find the original URL based on the short URL
    String findOriginalUrlByShortUrl(String shortUrl);

    // Insert a new URL mapping into the database
    void insertUrl(UrlTable url);

    // Get the click count for the short URL
    int getClickCountByShortUrl(@Param("shortUrl") String shortUrl);

    // Delete a specific short URL from the pre-generated URLs
    void deleteShortUrl(String shortUrl);

    // Retrieve the count of existing data in the pre_reload_urls table.
    int countPreReloadUrls();

    // Retrieve a specified number of random short URL from the pre-generated table (used for preloading).
    List<String> getRandomShortUrlsFromPreGenerated(@Param("count") int count);

    // Delete a list of short URLs from the pre-generated table.
    void deleteShortUrlsFromPreGenerated(@Param("shortUrls") List<String> shortUrls);

    // Insert the pre-generated short URL into the pre_reload_urls table.
    void preloadShortUrlToReload(String shortUrl);

    // Delete a specific pre-generated short URL from the pre_reload_urls table.
    void deleteShortUrlFromReload(String shortUrl);

    // Retrieve a random short URL from the pre-reload (used for getting short URL).
    String getRandomShortUrlFromReload();

    // Retrieve the URL entry based on the short URL
    UrlTable getUrlByShortUrl(String shortUrl);

    // Increment the click count for the short URL
    void incrementClickCount(@Param("shortUrl") String shortUrl);

    // Insert a pre-generated short URL into the database
    void insertPreGeneratedUrl(String shortUrl);

    // Check if a short URL exists in the database
    boolean existsShortUrl(String shortUrl);

    // data older than 2 years
    List<UrlTable> getOldUrls();

    // Insert old URL to pre_generated_urls
    void insertOldUrlToPreGenerated(@Param("shortUrl") String shortUrl);

    // Delete data older than 2 years
    void deleteOldUrls();
}
