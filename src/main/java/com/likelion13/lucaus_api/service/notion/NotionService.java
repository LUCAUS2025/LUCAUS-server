package com.likelion13.lucaus_api.service.notion;

import com.likelion13.lucaus_api.common.config.NotionConfig;
import com.likelion13.lucaus_api.domain.repository.detailedNotices.DetailedNoticesRepository;
import com.likelion13.lucaus_api.domain.repository.lostItems.LostItemsRepository;
import com.likelion13.lucaus_api.domain.repository.shortNotices.ShortNoticesRepository;
import com.likelion13.lucaus_api.dto.Notion.DetailedNoticeDto;
import com.likelion13.lucaus_api.dto.Notion.LostItemDto;
import com.likelion13.lucaus_api.dto.Notion.ShortNoticeDto;
import com.likelion13.lucaus_api.service.s3.S3Service;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.likelion13.lucaus_api.domain.entity.shortNotices.ShortNotices;
import com.likelion13.lucaus_api.domain.entity.detailedNotices.DetailedNotices;
import com.likelion13.lucaus_api.domain.entity.lostItems.LostItems;

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

    private ShortNoticeDto mapToShortNoticeDto(JSONObject page) {
        String uploadDateTimeString = page.optString("created_time");

        LocalDateTime createdTime = null;
        if (uploadDateTimeString != null && !uploadDateTimeString.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                createdTime = LocalDateTime.parse(uploadDateTimeString, formatter);
            } catch (Exception e) {
                logger.error("Failed to parse created_time: {}", uploadDateTimeString, e);
            }
        }

        JSONObject properties = page.optJSONObject("properties");

        JSONObject visibility = properties.optJSONObject("노출 여부");
        String visibilityName = visibility != null ? visibility.optJSONObject("select").optString("name") : null;

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
        String notionId = page.optString("id");

        return ShortNoticeDto.builder()
                .info(noticePlainText)
                .isVisible("True".equals(visibilityName))
                .uploadDateTime(createdTime)
                .notionId(notionId)
                .build();
    }


    private DetailedNoticeDto mapToDetailedNoticeDto(JSONObject page) {

        JSONObject properties = page.optJSONObject("properties");

        LocalDateTime uploadDateTime = null;
        JSONObject timeObj = properties.optJSONObject("등록 일시");

        if (timeObj != null) {
            JSONObject dateObj = timeObj.optJSONObject("date");
            if (dateObj != null) {
                String startDateStr = dateObj.optString("start");

                if (startDateStr != null && !startDateStr.isEmpty()) {
                    try {
                        LocalDate date = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        uploadDateTime = date.atStartOfDay();
                    } catch (Exception e) {
                        logger.error("Failed to parse 등록 일시: {}", startDateStr, e);
                    }
                }
            }
        }

        System.out.println("등록 일시");
        System.out.println(uploadDateTime);

        String category = null;
        if (properties != null) {
            JSONObject categoryObj = properties.optJSONObject("카테고리");
            if (categoryObj != null) {
                JSONArray categoryTextArray = categoryObj.optJSONArray("rich_text");
                if (categoryTextArray != null && categoryTextArray.length() > 0) {
                    category = categoryTextArray.optJSONObject(0)
                            .optJSONObject("text")
                            .optString("content");
                }
            }
        }

        String title = null;
        if (properties != null) {
            JSONObject titleObj = properties.optJSONObject("제목");
            if (titleObj != null) {
                JSONArray titleTextArray = titleObj.optJSONArray("title");
                if (titleTextArray != null && titleTextArray.length() > 0) {
                    JSONObject firstTextObj = titleTextArray.optJSONObject(0);
                    if (firstTextObj != null) {
                        title = firstTextObj.optString("plain_text");
                    }
                }
            }
        }

        StringBuilder contentBuilder = new StringBuilder();
        if (properties != null) {
            JSONObject contentObj = properties.optJSONObject("내용");
            if (contentObj != null) {
                JSONArray contentTextArray = contentObj.optJSONArray("rich_text");
                if (contentTextArray != null) {
                    for (int i = 0; i < contentTextArray.length(); i++) {
                        JSONObject contentTextObj = contentTextArray.optJSONObject(i);
                        if (contentTextObj != null) {
                            String contentText = contentTextObj.optJSONObject("text").optString("content");
                            contentBuilder.append(contentText).append("\n");
                        }
                    }
                }
            }
        }
        String content = contentBuilder.toString().trim();

        String photoUrl = null;
        JSONObject photoObj = properties.optJSONObject("사진");
        if (photoObj != null) {

            JSONArray filesArray = photoObj.optJSONArray("files");
            if (filesArray != null && filesArray.length() > 0) {

                JSONObject fileObj = filesArray.optJSONObject(0);
                if (fileObj != null) {

                    JSONObject file = fileObj.optJSONObject("file");
                    JSONObject file2 = fileObj.optJSONObject("external");

                    if (file != null) {
                        photoUrl = file.optString("url");
                    }
                    else if(file2 != null){
                        photoUrl = file2.optString("url");
                    }
                }
            }
        }



        String notionId = page.optString("id");

        Optional<DetailedNotices> existingDetailNotice = detailedNoticesRepository.findByNotionId(notionId);
        Pair<String, String> result = Pair.of("", "");

        if (photoUrl == null) {
            result = Pair.of("", "");
        } else {
            result = handleNotionPhotoUrl(photoUrl, existingDetailNotice);
        }

        return DetailedNoticeDto.builder()
                .notionId(notionId)
                .category(category)
                .title(title)
                .content(content)
                .photoUrl(result.getSecond())
                .notionPhotoUrl(result.getFirst())
                .uploadDateTime(uploadDateTime)
                .build();
    }

    private LostItemDto mapToLostItemDto(JSONObject page) {
        JSONObject properties = page.optJSONObject("properties");

        String notionId = page.optString("id");



        String category = "OTHERS";
        JSONObject categoryObj = properties.optJSONObject("카테고리");
        if (categoryObj != null) {
            JSONObject select = categoryObj.optJSONObject("select");
            if (select != null) {
                String categoryName = select.optString("name", "").toUpperCase();
                switch (categoryName) {
                    case "기타":
                        category = LostItems.Category.OTHERS.name();  // "OTHERS"로 설정
                        break;
                    case "잡화":
                        category = LostItems.Category.DAILY_NECESSITIES.name();  // "DAILY_NECESSITIES"로 설정
                        break;
                    case "전자기기":
                        category = LostItems.Category.ELECTRONICS.name();  // "ELECTRONICS"로 설정
                        break;
                    case "의류":
                        category = LostItems.Category.CLOTHING.name();  // "CLOTHING"으로 설정
                        break;
                    case "지갑/카드":
                        category = LostItems.Category.WALLET_CARD.name();  // "WALLET_CARD"로 설정
                        break;
                    default:
                        category = LostItems.Category.OTHERS.name();  // 기본값은 "OTHERS"
                        break;
                }
            }
        }

        String place = null;
        JSONObject placeObj = properties.optJSONObject("습득장소");
        if (placeObj != null) {
            JSONArray richTextArray = placeObj.optJSONArray("rich_text");
            if (richTextArray != null && richTextArray.length() > 0) {
                place = richTextArray.optJSONObject(0).optJSONObject("text").optString("content");
            }
        }

        LocalDateTime updatedDateTime = null;
        JSONObject timeObj = properties.optJSONObject("접수 일자");

        if (timeObj != null) {
            JSONObject dateObj = timeObj.optJSONObject("date");
            if (dateObj != null) {
                String startDateStr = dateObj.optString("start");

                if (startDateStr != null && !startDateStr.isEmpty()) {
                    try {
                        LocalDate date = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        updatedDateTime = date.atStartOfDay();
                    } catch (Exception e) {
                        logger.error("Failed to parse 접수 일자: {}", startDateStr, e);
                    }
                }
            }
        }

        boolean ownerFound = false;
        JSONObject ownerFoundObj = properties.optJSONObject("주인 찾음");
        if (ownerFoundObj != null) {
            JSONObject selectObj = ownerFoundObj.optJSONObject("select");
            if (selectObj != null) {
                String ownerFoundStatus = selectObj.optString("name");
                ownerFound = "주인 찾음".equals(ownerFoundStatus);  // "주인 찾음"이면 true
            }
        }

        String name = null;
        JSONObject nameObj = properties.optJSONObject("물품명");
        if (nameObj != null) {
            JSONArray titleArray = nameObj.optJSONArray("title");
            if (titleArray != null && titleArray.length() > 0) {
                name = titleArray.optJSONObject(0).optJSONObject("text").optString("content");
            }
        }

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

        Optional<LostItems> existingLostItem = lostItemsRepository.findByNotionId(notionId);

        Pair<String, String> result = Pair.of("", "");

        if (photoUrl == null) {
            result = Pair.of("", "");
        } else {
            String notionPhotoUrl = photoUrl;
            result = handleNotionPhotoUrl(notionPhotoUrl, existingLostItem);
        }

        return LostItemDto.builder()
                .notionId(notionId)
                .updatedDateTime(updatedDateTime)
                .category(category)
                .place(place)
                .photoUrl(result.getSecond())
                .ownerFound(ownerFound)
                .name(name)
                .notionPhotoUrl(result.getFirst())
                .build();
    }


    //     ShortNotice 데이터를 주기적으로 가져오는 메서드

    @Scheduled(cron = "0 * * * * *")
    public void fetchShortNotices() {
        List<ShortNoticeDto> shortNotices = fetchNotionDataFromDatabaseId(notionConfig.getShortNoticesDbId(), ShortNoticeDto.class);

        for (ShortNoticeDto shortNoticeDto : shortNotices) {
            Optional<ShortNotices> existingShortNotice = shortNoticesRepository.findByNotionId(shortNoticeDto.getNotionId());

            ShortNotices shortNoticesEntity;

            if (existingShortNotice.isPresent()) {
                shortNoticesEntity = existingShortNotice.get();
                shortNoticesEntity.changeInfo(shortNoticeDto.getInfo());
                shortNoticesEntity.changeIsVisible(shortNoticeDto.isVisible());
            } else {
                shortNoticesEntity = ShortNotices.builder()
                        .info(shortNoticeDto.getInfo())
                        .isVisible(shortNoticeDto.isVisible())
                        .uploadDateTime(shortNoticeDto.getUploadDateTime())
                        .notionId(shortNoticeDto.getNotionId())
                        .build();
            }

            shortNoticesRepository.save(shortNoticesEntity);
        }
    }


    // DetailedNotice 데이터를 주기적으로 가져오는 메서드
//    @Scheduled(cron = "0 */10 * * * *")
    @Scheduled(cron = "0 * * * * *")
    public void fetchDetailedNotices() {

        List<DetailedNoticeDto> detailedNotices = fetchNotionDataFromDatabaseId(notionConfig.getDetailedNoticesDbId(), DetailedNoticeDto.class);

        for (DetailedNoticeDto detailedNoticeDto : detailedNotices) {
            Optional<DetailedNotices> existingDetailedNotice = detailedNoticesRepository.findByNotionId(detailedNoticeDto.getNotionId());

            DetailedNotices detailedNoticesEntity;

            if (existingDetailedNotice.isPresent()) {
                detailedNoticesEntity = existingDetailedNotice.get();
                detailedNoticesEntity.changeCategory(detailedNoticeDto.getCategory());
                detailedNoticesEntity.changeTitle(detailedNoticeDto.getTitle());
                detailedNoticesEntity.changeContent(detailedNoticeDto.getContent());
                detailedNoticesEntity.changePhotoUrl(detailedNoticeDto.getPhotoUrl());
                detailedNoticesEntity.changeNotionPhotoUrl(detailedNoticeDto.getNotionPhotoUrl());
                detailedNoticesEntity.changeuploadDateTime(detailedNoticeDto.getUploadDateTime());
            } else {
                detailedNoticesEntity = DetailedNotices.builder()
                        .category(detailedNoticeDto.getCategory())
                        .title(detailedNoticeDto.getTitle())
                        .content(detailedNoticeDto.getContent())
                        .photoUrl(detailedNoticeDto.getPhotoUrl())
                        .notionId(detailedNoticeDto.getNotionId())
                        .notionPhotoUrl(detailedNoticeDto.getNotionPhotoUrl())
                        .uploadDateTime(detailedNoticeDto.getUploadDateTime())
                        .build();
            }

            detailedNoticesRepository.save(detailedNoticesEntity);
        }
    }


    // LostItem 데이터를 주기적으로 가져오는 메서드
//@Scheduled(cron = "0 * * * * *")
@Scheduled(cron = "0 */10 * * * *")
    public void fetchLostItems() {
        List<LostItemDto> lostItems = fetchNotionDataFromDatabaseId(notionConfig.getLostItemsDbId(), LostItemDto.class);

        for (LostItemDto lostItemDto : lostItems) {
            Optional<LostItems> existingLostItem = lostItemsRepository.findByNotionId(lostItemDto.getNotionId());

            LostItems lostItemEntity;

            String category = lostItemDto.getCategory();
            LostItems.Category categoryEnum = LostItems.Category.OTHERS; // 기본값 설정

            if (category != null) {
                try {
                    categoryEnum = LostItems.Category.valueOf(category.toUpperCase());
                } catch (IllegalArgumentException e) {
                    categoryEnum = LostItems.Category.OTHERS;
                }
            }

            if (existingLostItem.isPresent()) {
                lostItemEntity = existingLostItem.get();
                lostItemEntity.changePlace(lostItemDto.getPlace());
                lostItemEntity.changeName(lostItemDto.getName());
                lostItemEntity.changePhotoUrl(lostItemDto.getPhotoUrl());
                lostItemEntity.changeNotionPhotoUrl(lostItemDto.getNotionPhotoUrl());
                lostItemEntity.changeCategory(categoryEnum);
                lostItemEntity.changeOwnerFound(lostItemDto.isOwnerFound());
            } else {
                lostItemEntity = LostItems.builder()
                        .updatedDateTime(lostItemDto.getUpdatedDateTime())
                        .place(lostItemDto.getPlace())
                        .name(lostItemDto.getName())
                        .photoUrl(lostItemDto.getPhotoUrl())
                        .notionPhotoUrl(lostItemDto.getNotionPhotoUrl())
                        .category(categoryEnum)
                        .ownerFound(lostItemDto.isOwnerFound())
                        .notionId(lostItemDto.getNotionId())
                        .build();
            }
            lostItemsRepository.save(lostItemEntity);
        }
    }
    public Pair<String, String> handleNotionPhotoUrl(String notionPhotoUrl, Optional<?> existingItem) {
        String photoUrl = null;

        if (existingItem.isPresent()) {
            if (existingItem.get() instanceof LostItems) {
                LostItems existingLostItem = (LostItems) existingItem.get();
                String existingNotionPhotoUrl = existingLostItem.getNotionPhotoUrl();

                // 기존 LostItems에 대한 처리
                if (existingNotionPhotoUrl != null && notionPhotoUrl != null && existingNotionPhotoUrl.length() >= 200 &&
                        existingNotionPhotoUrl.substring(0, 200).equals(notionPhotoUrl.substring(0, 200))) {
                    notionPhotoUrl = existingNotionPhotoUrl;
                    photoUrl = existingLostItem.getPhotoUrl();
                } else {
                    if (existingLostItem.getUpdatedDateTime().plusHours(1).isAfter(LocalDateTime.now())) {
                        try {
                            photoUrl = s3Service.downloadAndUploadImage(notionPhotoUrl, "lost");
                            if (notionPhotoUrl != null && notionPhotoUrl.length() > 200) {
                                notionPhotoUrl = notionPhotoUrl.substring(0, 200);
                            }
                        } catch (IOException e) {
                            photoUrl = existingNotionPhotoUrl;
                            if (notionPhotoUrl != null && notionPhotoUrl.length() > 200) {
                                notionPhotoUrl = notionPhotoUrl.substring(0, 200);
                            }
                        }
                    } else {
                        notionPhotoUrl = existingNotionPhotoUrl;
                        photoUrl = existingLostItem.getPhotoUrl();
                    }
                }
            } else if (existingItem.get() instanceof DetailedNotices) {
                DetailedNotices existingDetailedNotice = (DetailedNotices) existingItem.get();
                String existingNotionPhotoUrl = existingDetailedNotice.getNotionPhotoUrl();

                if (existingNotionPhotoUrl != null && notionPhotoUrl != null && existingNotionPhotoUrl.length() >= 200 &&
                        existingNotionPhotoUrl.substring(0, 200).equals(notionPhotoUrl.substring(0, 200))) {
                    notionPhotoUrl = existingNotionPhotoUrl;
                    photoUrl = existingDetailedNotice.getPhotoUrl();
                } else {
                    if (existingDetailedNotice.getUploadDateTime().plusHours(1).isAfter(LocalDateTime.now())) {
                        try {
                            photoUrl = s3Service.downloadAndUploadImage(notionPhotoUrl, "notice");
                            if (notionPhotoUrl != null && notionPhotoUrl.length() > 200) {
                                notionPhotoUrl = notionPhotoUrl.substring(0, 200);
                            }
                        } catch (IOException e) {
                            photoUrl = existingNotionPhotoUrl;
                            if (notionPhotoUrl != null && notionPhotoUrl.length() > 200) {
                                notionPhotoUrl = notionPhotoUrl.substring(0, 200);
                            }
                        }
                    } else {
                        notionPhotoUrl = existingNotionPhotoUrl;
                        photoUrl = existingDetailedNotice.getPhotoUrl();
                    }
                }
            }
        } else {
            try {
                photoUrl = s3Service.downloadAndUploadImage(notionPhotoUrl, "etc");
                if (notionPhotoUrl != null && notionPhotoUrl.length() > 200) {
                    notionPhotoUrl = notionPhotoUrl.substring(0, 200);
                }
            } catch (IOException e) {
                logger.error("이미지 다운로드 및 업로드 중 오류 발생", e);
            }
        }
        if (notionPhotoUrl == null) {
            notionPhotoUrl = "";  // 기본 값 설정
        }
        if (photoUrl == null) {
            photoUrl = "";  // 기본 값 설정
        }

        return Pair.of(notionPhotoUrl, photoUrl);
    }


}
