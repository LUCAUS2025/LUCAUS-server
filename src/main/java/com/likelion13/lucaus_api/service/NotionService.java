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
            logger.info("page mapper 결과 : {}",mapper.apply(page));
            dtoList.add(mapper.apply(page));
        }
        logger.info("maptolist 결과 : {}",dtoList.toString());
        return dtoList;
    }

    public <T> List<T> fetchNotionDataFromDatabaseId(String databaseId, Class<T> dtoClass) {
        String url = "https://api.notion.com/v1/databases/" + databaseId + "/query";
        logger.info("Fetching data from Notion database with ID: {}", databaseId);
        logger.info("Constructed URL: {}", url);

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

                logger.info("jsonResponse: {}", jsonResponse.toString());
                logger.info("results: {}", results.toString());

                Function<JSONObject, T> mapper = getMapper(dtoClass);
//                for (int i = 0; i < results.length(); i++) {
//                    JSONObject page = results.getJSONObject(i);
//                    T mappedResult = mapper.apply(page);
//                    logger.info("Mapped result {}: {}", i, mappedResult.toString());
//                }

                return mapToList(results, mapper);
            } else {
                logger.info("No data found for database ID: {}", databaseId);
                return new ArrayList<>();
            }
        } catch (Exception e) {
            logger.info("Error fetching data from Notion database with ID: {} - {}", databaseId, e.getMessage());
            throw new RuntimeException("Failed to fetch data from Notion database ID: " + databaseId, e);
        }
    }

    private <T> Function<JSONObject, T> getMapper(Class<T> dtoClass) {
        logger.info("Selected DTO class: {}", dtoClass.getSimpleName());
        if (dtoClass.equals(ShortNoticeDto.class)) {
            logger.info("shornotice dto 선택");
            return json -> dtoClass.cast(mapToShortNoticeDto(json));
        } else if (dtoClass.equals(DetailedNoticeDto.class)) {
            logger.info("DetailedNoticeDto 선택");

            return json -> dtoClass.cast(mapToDetailedNoticeDto(json));
        } else if (dtoClass.equals(LostItemDto.class)) {
            logger.info("LostItemDto 선택");

            return json -> dtoClass.cast(mapToLostItemDto(json));
        } else {
            throw new IllegalArgumentException("Unsupported DTO type: " + dtoClass);
        }
    }

    // ShortNoticeDto로 변환하는 로직
    private ShortNoticeDto mapToShortNoticeDto(JSONObject page) {
        // 필요한 속성들을 JSON에서 추출하고 ShortNoticeDto로 변환
        JSONObject properties = page.optJSONObject("properties");

        // "노출 여부" -> select -> name 추출
        JSONObject visibility = properties.optJSONObject("노출 여부");
        String visibilityName = visibility != null ? visibility.optJSONObject("select").optString("name") : null;

        // "안내글" -> title -> text -> plain_text 추출
        JSONObject noticeText = properties.optJSONObject("안내글");
        String noticePlainText = null;
        if (noticeText != null) {
            JSONArray titleArray = noticeText.optJSONArray("title");
            if (titleArray != null && titleArray.length() > 0) {
                noticePlainText = titleArray.optJSONObject(0)
                        .optJSONObject("text")
                        .optString("content");
            }
        }

        String createdTimeString = page.optString("created_time");
        LocalDateTime createdTime = null;
        if (createdTimeString != null && !createdTimeString.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                createdTime = LocalDateTime.parse(createdTimeString, formatter);
            } catch (Exception e) {
                logger.error("Failed to parse created_time: {}", createdTimeString, e);
            }
        }
        String notionId = page.optString("id");

        // ShortNoticeDto 객체 생성
        return ShortNoticeDto.builder()
                .info(noticePlainText)
                .isVisible("True".equals(visibilityName))
                .uploadDateTime(createdTime)
                .notionId(notionId)
                .build();
    }


    // DetailedNoticeDto로 변환하는 로직
    private DetailedNoticeDto mapToDetailedNoticeDto(JSONObject page) {
        // 필요한 속성들을 JSON에서 추출하고 DetailedNoticeDto로 변환
        String uploadDateTimeString = page.optString("uploadDateTime");

        // uploadDateTime을 LocalDateTime으로 변환
        LocalDateTime uploadDateTime = null;
        if (uploadDateTimeString != null && !uploadDateTimeString.isEmpty()) {
            try {
                // 예시: "yyyy-MM-dd'T'HH:mm:ss" 형식에 맞게 LocalDateTime으로 변환
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                uploadDateTime = LocalDateTime.parse(uploadDateTimeString, formatter);
            } catch (Exception e) {
                // 변환 실패 시 로그를 남기고 null로 설정
                logger.error("Failed to parse uploadDateTime: {}", uploadDateTimeString, e);
            }
        }

        return DetailedNoticeDto.builder()
                .category(page.optString("category"))
                .title(page.optString("title"))
                .content(page.optString("content"))
                .photoUrl(page.optString("photoUrl"))
                .uploadDateTime(uploadDateTime)
                .build();
    }

    private LostItemDto mapToLostItemDto(JSONObject page) {
        // 기본적으로 필요한 속성들을 추출
        JSONObject properties = page.optJSONObject("properties");

        // notionId는 페이지의 ID
        String notionId = page.optString("id");

        // updatedDateTime (created_at)
        String updatedTimeString = page.optString("created_time");
        LocalDateTime updatedDateTime = null;
        if (updatedTimeString != null && !updatedTimeString.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                updatedDateTime = LocalDateTime.parse(updatedTimeString, formatter);
            } catch (Exception e) {
                logger.error("Failed to parse created_time: {}", updatedTimeString, e);
            }
        }

        // category (카테고리)
        String category = "OTHERS";  // 기본값 설정
        JSONObject categoryObj = properties.optJSONObject("카테고리");
        if (categoryObj != null) {
            JSONObject select = categoryObj.optJSONObject("select");
            if (select != null) {
                String categoryName = select.optString("name", "").toUpperCase();
                switch (categoryName) {
                    case "기타":
                        category = "OTHERS";
                        break;
                    case "화장품":
                        category = "COSMETICS";
                        break;
                    case "전자기기":
                        category = "ELECTRONICS";
                        break;
                    case "의류":
                        category = "CLOTHING";
                        break;
                    case "지갑/카드":
                        category = "WALLET_CARD";
                        break;
                }
            }
        }

        // place (습득장소)
        String place = null;
        JSONObject placeObj = properties.optJSONObject("습득장소");
        if (placeObj != null) {
            JSONArray richTextArray = placeObj.optJSONArray("rich_text");
            if (richTextArray != null && richTextArray.length() > 0) {
                place = richTextArray.optJSONObject(0).optJSONObject("text").optString("content");
            }
        }

        // photoUrl (사진)
        String photoUrl = null;
        JSONObject photoObj = properties.optJSONObject("사진");
        if (photoObj != null) {
            JSONArray filesArray = photoObj.optJSONArray("files");
            if (filesArray != null && filesArray.length() > 0) {
                JSONObject fileObj = filesArray.optJSONObject(0);
                if (fileObj != null) {
                    JSONObject file = fileObj.optJSONObject("file");
                    if (file != null) {
                        photoUrl = file.optString("url");
                    }
                }
            }
        }
        String s3ImageUrl=null;
        try {
            // S3Service 클래스의 메서드 호출
            String s3Url = s3Service.downloadAndUploadImage(photoUrl, "lost");
            logger.info("S3 URL: " + s3Url);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("이미지 다운로드 및 업로드 중 오류 발생");
        }


        // ownerFound (주인 찾음)
        boolean ownerFound = false;
        JSONObject ownerFoundObj = properties.optJSONObject("주인 찾음");
        if (ownerFoundObj != null) {
            JSONObject selectObj = ownerFoundObj.optJSONObject("select");
            if (selectObj != null) {
                String ownerFoundStatus = selectObj.optString("name");
                ownerFound = "주인 찾음".equals(ownerFoundStatus);  // "주인 찾음"이면 true
            }
        }

        // name (물품명)
        String name = null;
        JSONObject nameObj = properties.optJSONObject("물품명");
        if (nameObj != null) {
            JSONArray titleArray = nameObj.optJSONArray("title");
            if (titleArray != null && titleArray.length() > 0) {
                name = titleArray.optJSONObject(0).optJSONObject("text").optString("content");
            }
        }

        // LostItemDto 객체 생성
        return LostItemDto.builder()
                .notionId(notionId)
                .updatedDateTime(updatedDateTime)
                .category(category)
                .place(place)
                .photoUrl(s3ImageUrl)
                .ownerFound(ownerFound)
                .name(name)
                .build();
    }



    // ShortNotice 데이터를 주기적으로 가져오는 메서드
//    @Scheduled(cron = "0 * * * * *")
//    public void fetchShortNotices() {
//        List<ShortNoticeDto> shortNotices = fetchNotionDataFromDatabaseId(notionConfig.getShortNoticesDbId(), ShortNoticeDto.class);
//        logger.info("shortNotices: {}", shortNotices.toString());
//
//        for (ShortNoticeDto shortNoticeDto : shortNotices) {
//            Optional<ShortNotices> existingShortNotice = shortNoticesRepository.findByNotionId(shortNoticeDto.getNotionId());
//
//            ShortNotices shortNoticesEntity;
//
//            if (existingShortNotice.isPresent()) {
//                shortNoticesEntity = existingShortNotice.get();
//                shortNoticesEntity.changeInfo(shortNoticeDto.getInfo());
//                shortNoticesEntity.changeIsVisible(shortNoticeDto.isVisible());
//            } else {
//                shortNoticesEntity = ShortNotices.builder()
//                        .info(shortNoticeDto.getInfo())
//                        .isVisible(shortNoticeDto.isVisible())
//                        .uploadDateTime(shortNoticeDto.getUploadDateTime())
//                        .notionId(shortNoticeDto.getNotionId())
//                        .build();
//            }
//
//            shortNoticesRepository.save(shortNoticesEntity);
//        }
//    }


//    // DetailedNotice 데이터를 주기적으로 가져오는 메서드
//    @Scheduled(cron = "0 * * * * *")
//    public void fetchDetailedNotices() {
//        List<DetailedNoticeDto> detailedNotices = fetchNotionDataFromDatabaseId(notionConfig.getDetailedNoticesDbId(), DetailedNoticeDto.class);
//        logger.info("detailedNotices: {}", detailedNotices.toString());
//        for (DetailedNoticeDto detailedNoticeDto : detailedNotices) {
//            detailedNoticesRepository.save(
//                    DetailedNotices.builder()
//                            .category(detailedNoticeDto.getCategory())
//                            .title(detailedNoticeDto.getTitle())
//                            .content(detailedNoticeDto.getContent())
//                            .photoUrl(detailedNoticeDto.getPhotoUrl())
//                            .uploadDateTime(detailedNoticeDto.getUploadDateTime())
//                            .build()
//            );
//        }
//    }
//
    // LostItem 데이터를 주기적으로 가져오는 메서드
@Scheduled(cron = "0 * * * * *")
public void fetchLostItems() {
    List<LostItemDto> lostItems = fetchNotionDataFromDatabaseId(notionConfig.getLostItemsDbId(), LostItemDto.class);
    logger.info("lostItems: {}", lostItems.toString());

    for (LostItemDto lostItemDto : lostItems) {
        // notionId로 기존 데이터를 찾음
        Optional<LostItems> existingLostItem = lostItemsRepository.findByNotionId(lostItemDto.getNotionId());

        LostItems lostItemEntity;

        // category 값 처리
        String category = lostItemDto.getCategory();
        LostItems.Category categoryEnum = LostItems.Category.OTHERS; // 기본값 설정

        if (category != null) {
            try {
                // 대문자로 변환 후 enum 값으로 변환
                categoryEnum = LostItems.Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                // enum 변환 실패시 기본값 사용
                logger.info("Invalid category value: {}. Defaulting to OTHERS.", category);
            }
        }

        if (existingLostItem.isPresent()) {
            // 기존 데이터가 있으면 해당 엔터티를 수정
            lostItemEntity = existingLostItem.get();
            lostItemEntity.changePlace(lostItemDto.getPlace());
            lostItemEntity.changeName(lostItemDto.getName());
            lostItemEntity.changePhotoUrl(lostItemDto.getPhotoUrl());
            lostItemEntity.changeCategory(categoryEnum);  // 카테고리 수정
            lostItemEntity.changeOwnerFound(lostItemDto.isOwnerFound());
        } else {
            // 새 데이터가 있으면 새 엔터티 생성
            lostItemEntity = LostItems.builder()
                    .updatedDateTime(lostItemDto.getUpdatedDateTime())
                    .place(lostItemDto.getPlace())
                    .name(lostItemDto.getName())
                    .photoUrl(lostItemDto.getPhotoUrl())
                    .category(categoryEnum)  // 카테고리 설정
                    .ownerFound(lostItemDto.isOwnerFound())
                    .notionId(lostItemDto.getNotionId())
                    .build();
        }

        lostItemsRepository.save(lostItemEntity);
    }
}

}
