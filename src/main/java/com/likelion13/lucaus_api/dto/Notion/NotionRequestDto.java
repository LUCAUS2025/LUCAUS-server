package com.likelion13.lucaus_api.dto.Notion;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotionRequestDto {

    private String databaseId;
    private String filter;

    @Builder
    public NotionRequestDto(String databaseId, String filter) {
        this.databaseId = databaseId;
        this.filter = filter;
    }
}
