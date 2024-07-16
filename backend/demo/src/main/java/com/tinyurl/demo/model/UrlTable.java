package com.tinyurl.demo.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UrlTable {
  private Long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime creationDate; 
}
