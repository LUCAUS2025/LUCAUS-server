package com.likelion13.lucaus_api.service;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
public class S3Service {

    @Value("${aws.accessKey}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String secretAccessKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.bucketName}")
    private String bucketName;

    // S3Client를 직접 생성하는 방식
    private S3Client s3Client;

    public S3Service() {

        this.s3Client = S3Client.builder()
                .region(Region.of(region))  // aws.region 값을 사용
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();
    }

    // 이미지 다운로드 및 S3 업로드 메서드, 반환값: S3 URL
    public String downloadAndUploadImage(String imageUrl, String folderName) throws IOException {
        // 사용자 홈 디렉토리 뒤에 Desktop 폴더를 추가하여 경로 설정
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "downloaded_image.jpg";

        // 이미지 다운로드
        downloadImage(imageUrl, desktopPath);

        // UUID를 사용하여 고유한 objectKey 생성 (폴더명 + UUID)
        String objectKey = folderName + "/" + UUID.randomUUID().toString() + ".jpg";

        // S3에 업로드
        uploadToS3(desktopPath, objectKey);

        // S3 URL 반환 (업로드 후 URL 생성)
        return generateS3Url(objectKey);
    }

    // 이미지를 다운로드하는 메소드
    private static void downloadImage(String imageUrl, String destinationPath) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream();
             FileOutputStream fileOutputStream = new FileOutputStream(destinationPath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    // 이미지를 S3에 업로드하는 메소드
    private void uploadToS3(String filePath, String objectKey) {
        File file = new File(filePath);

        try {
            // 파일 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            // 파일을 S3에 업로드
            s3Client.putObject(putObjectRequest, file.toPath());
        } catch (S3Exception e) {
            e.printStackTrace();
            System.out.println("S3 업로드 실패: " + e.awsErrorDetails().errorMessage());
        }
    }

    // S3 URL 생성 메소드
    private String generateS3Url(String objectKey) {
        // S3 URL 형식: https://<버킷명>.s3.<리전>.amazonaws.com/<객체키>
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, objectKey);
    }
}
