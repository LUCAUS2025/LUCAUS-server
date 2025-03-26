package com.likelion13.lucaus_api.service;

import com.likelion13.lucaus_api.common.config.NotionConfig;
import com.likelion13.lucaus_api.domain.repository.DetailedNoticesRepository;
import com.likelion13.lucaus_api.domain.repository.LostItemsRepository;
import com.likelion13.lucaus_api.domain.repository.ShortNoticesRepository;
import com.likelion13.lucaus_api.dto.Notion.DetailedNoticeDto;
import com.likelion13.lucaus_api.dto.Notion.LostItemDto;
import com.likelion13.lucaus_api.dto.Notion.ShortNoticeDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.likelion13.lucaus_api.domain.entity.ShortNotices;
import com.likelion13.lucaus_api.domain.entity.DetailedNotices;
import com.likelion13.lucaus_api.domain.entity.LostItems;

@Service
public class NotionService {

    private static final Logger logger = LoggerFactory.getLogger(NotionService.class);

    private final RestTemplate restTemplate;
    private final NotionConfig notionConfig;
    private final ShortNoticesRepository shortNoticesRepository;
    private final DetailedNoticesRepository detailedNoticesRepository;
    private final LostItemsRepository lostItemsRepository;

    private final S3Service s3Service;


    @Autowired
    public NotionService(RestTemplate restTemplate, NotionConfig notionConfig, ShortNoticesRepository shortNoticesRepository, DetailedNoticesRepository detailedNoticesRepository, LostItemsRepository lostItemsRepository,S3Service s3Service) {
        this.restTemplate = restTemplate;
        this.notionConfig = notionConfig;
        this.shortNoticesRepository = shortNoticesRepository;
        this.detailedNoticesRepository = detailedNoticesRepository;
        this.lostItemsRepository = lostItemsRepository;
        this.s3Service = s3Service;
    }

    private <T> List<T> mapToList(JSONArray results, Function<JSONObject, T> mapper) {
        List<T> dtoList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject page = results.getJSONObject(i);
            dtoList.add(mapper.apply(page));
        }

        return dtoList;
    }

    public <T> List<T> fetchNotionDataFromDatabaseId(String databaseId, Class<T> dtoClass) {
        String url = "https://api.notion.com/v1/databases/" + databaseId + "/query";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + notionConfig.getApiKey());
        headers.set("Notion-Version", "2022-06-28");
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getBody() != null) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray results = jsonResponse.getJSONArray("results");

                Function<JSONObject, T> mapper = getMapper(dtoClass);

                return mapToList(results, mapper);
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            logger.info("error occur : {}",e.toString());
            throw new RuntimeException("Failed to fetch data from Notion database ID: " + databaseId);
        }
    }

    private <T> Function<JSONObject, T> getMapper(Class<T> dtoClass) {
        if (dtoClass.equals(ShortNoticeDto.class)) {
            return json -> dtoClass.cast(mapToShortNoticeDto(json));
        } else if (dtoClass.equals(DetailedNoticeDto.class)) {
            return json -> dtoClass.cast(mapToDetailedNoticeDto(json));
        } else if (dtoClass.equals(LostItemDto.class)) {
            return json -> dtoClass.cast(mapToLostItemDto(json));
        } else {
            throw new IllegalArgumentException("Unsupported DTO type: " + dtoClass);
        }
    }


}
