package com.likelion13.lucaus_api.dto.Notion;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ShortNoticeDto {
    private String info;
    private String notionId;
    private boolean isVisible;
    private LocalDateTime uploadDateTime;

    @Override
    public String toString() {
        return "ShortNoticeDto{" +
                "info='" + info + '\'' +
                ", isVisible=" + isVisible +
                ", uploadDateTime=" + uploadDateTime +
                ", notionId=" + notionId +
                '}';
    }

}
