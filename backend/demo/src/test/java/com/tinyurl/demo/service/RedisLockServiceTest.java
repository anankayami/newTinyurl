package com.tinyurl.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RedisLockServiceTest {
  
    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisLockService redisLockService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testAcquireLock_Success() {
        String key = "lock:testKey";
        String value = "testValue";
        long timeout = 10;

        when(valueOperations.setIfAbsent(key, value, timeout, TimeUnit.SECONDS)).thenReturn(true);

        boolean result = redisLockService.acquireLock(key, value, timeout);

        assertTrue(result);
        verify(valueOperations, times(1)).setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
    }

    @Test
    public void testAcquireLock_Failure() {
        String key = "lock:testKey";
        String value = "testValue";
        long timeout = 10;

        when(valueOperations.setIfAbsent(key, value, timeout, TimeUnit.SECONDS)).thenReturn(false);

        boolean result = redisLockService.acquireLock(key, value, timeout);

        assertFalse(result);
        verify(valueOperations, times(1)).setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
    }

    @Test
    public void testReleaseLock_Success() {
        String key = "lock:testKey";
        String value = "testValue";

        when(valueOperations.get(key)).thenReturn(value);

        redisLockService.releaseLock(key, value);

        verify(redisTemplate, times(1)).delete(key);
    }

    @Test
    public void testReleaseLock_Failure() {
        String key = "lock:testKey";
        String value = "testValue";

        when(valueOperations.get(key)).thenReturn("differentValue");

        redisLockService.releaseLock(key, value);

        verify(redisTemplate, never()).delete(key);
    }
}
