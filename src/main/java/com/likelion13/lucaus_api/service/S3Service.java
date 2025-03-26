package com.likelion13.lucaus_api.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
public class S3Service {

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    private final S3Client s3Client;

    public S3Service(@Qualifier("s3Client") S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // 이미지 다운로드 및 S3 업로드 메서드, 반환값: S3 URL
    public String downloadAndUploadImage(String imageUrl, String folderName) throws IOException {
        // UUID를 사용하여 고유한 objectKey 생성 (폴더명 + UUID)
        String objectKey = folderName + "/" + UUID.randomUUID().toString()+".png";

        // 이미지 URL에서 InputStream을 직접 얻어와서 S3에 업로드
        uploadToS3FromUrl(imageUrl, objectKey);

        // S3 URL 반환 (업로드 후 URL 생성)
        return generateS3Url(objectKey);
    }

    // URL에서 이미지를 다운로드하여 S3에 업로드하는 메소드
    private void uploadToS3FromUrl(String imageUrl, String objectKey) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream()) {
            // S3에 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .acl("public-read")  // 객체를 퍼블릭 읽기 가능하게 설정
                    .build();

            // S3에 업로드 (InputStream을 사용)
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, url.openConnection().getContentLengthLong()));
        } catch (S3Exception e) {
            System.err.println("S3 업로드 실패: " + e.awsErrorDetails().errorMessage());
            e.printStackTrace();
        }
    }

    // S3 URL 생성 메소드
    private String generateS3Url(String objectKey) {
        // 업로드한 객체에 접근할 수 있는 공개 URL 반환
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, objectKey);
    }
}