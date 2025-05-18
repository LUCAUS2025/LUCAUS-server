package com.likelion13.lucaus_api.service.s3;

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

    @Value("${cloudfront.domainName}")
    private String cloudfrontDomainName;

    private final S3Client s3Client;

    public S3Service(@Qualifier("s3Client") S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // 이미지 다운로드 및 S3 업로드 메서드, 반환값: S3 URL
    public String downloadAndUploadImage(String imageUrl, String folderName) throws IOException {

        String objectKey = folderName + "/" + UUID.randomUUID().toString()+".png";

        uploadToS3FromUrl(imageUrl, objectKey);

        return generateS3Url(objectKey);
    }

    private void uploadToS3FromUrl(String imageUrl, String objectKey) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .acl("public-read")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, url.openConnection().getContentLengthLong()));
        } catch (S3Exception e) {
            System.err.println("S3 업로드 실패: " + e.awsErrorDetails().errorMessage());
            e.printStackTrace();
        }
    }

    private String generateS3Url(String objectKey) {
        return String.format("https://%s/%s", cloudfrontDomainName, objectKey);
    }
}