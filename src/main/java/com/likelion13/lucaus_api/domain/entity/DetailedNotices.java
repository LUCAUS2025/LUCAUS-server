package com.likelion13.lucaus_api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DetailedNotices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String notionId;
    private String category;
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private String photoUrl;
    private String notionPhotoUrl;
    private LocalDateTime uploadDateTime;

    public void changeCategory(String category) {
        this.category = category;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeNotionPhotoUrl(String notionPhotoUrl) {
        this.notionPhotoUrl = notionPhotoUrl;
    }

    public void changePhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }



}
