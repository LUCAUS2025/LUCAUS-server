package com.likelion13.lucaus_api.dto.Notion;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DetailedNoticeDto {
    private String category;
    private String title;
    private String content;
    private String photoUrl;
    private LocalDateTime uploadDateTime;
    private LocalDateTime createdDateTime;
    private String notionPhotoUrl;
    private String notionId;

    @Override
    public String toString() {
        return "DetailedNoticeDto{" +
                "category='" + category + '\'' +
                ", notionId=" + notionId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", uploadDateTime=" + uploadDateTime +
                '}';
    }

}
