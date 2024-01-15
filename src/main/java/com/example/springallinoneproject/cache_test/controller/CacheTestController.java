package com.example.springallinoneproject.cache_test.controller;

import com.example.springallinoneproject.cache_test.dto.CacheTestDTO;
import com.example.springallinoneproject.cache_test.service.CacheTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CacheTestController {
    private final CacheTestService cacheTestService;

    @Cacheable(value = "CacheTestDTO", key = "#id", cacheManager = "customCacheManager", unless = "#id==''", condition = "#id.length > 2")
    @GetMapping("/result")
    public CacheTestDTO result(@RequestParam String id) {
        return cacheTestService.getResult(id);
    }
}
