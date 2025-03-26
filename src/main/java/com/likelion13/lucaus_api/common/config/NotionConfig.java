package com.likelion13.lucaus_api.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class NotionConfig {

    @Value("${notion.api-key}")
    private String apiKey;

    @Value("${notion.database.detailed-notices}")
    private String detailedNoticesDbId;

    @Value("${notion.database.short-notices}")
    private String shortNoticesDbId;

    @Value("${notion.database.lost-items}")
    private String lostItemsDbId;
}
