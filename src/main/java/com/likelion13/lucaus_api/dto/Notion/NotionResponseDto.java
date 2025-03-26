package com.likelion13.lucaus_api.dto.Notion;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class NotionResponseDto {

    private List<Result> results;

    @Getter
    @NoArgsConstructor
    public static class Result {
        private String id;
        private Map<String, Property> properties;
    }

    @Getter
    @NoArgsConstructor
    public static class Property {
        private String type;
        private Title title;
        private RichText rich_text;

        @Getter
        @NoArgsConstructor
        public static class Title {
            private List<Text> text;
        }

        @Getter
        @NoArgsConstructor
        public static class RichText {
            private List<Text> text;
        }

        @Getter
        @NoArgsConstructor
        public static class Text {
            private String content;
        }
    }
}
