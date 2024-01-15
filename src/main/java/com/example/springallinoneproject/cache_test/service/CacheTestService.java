package com.example.springallinoneproject.cache_test.service;

import com.example.springallinoneproject.cache_test.dto.CacheTestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheTestService {
    public CacheTestDTO getResult(String id) {
        log.info("Call get Result");
        return new CacheTestDTO(id, id + "example content");
    }
}
