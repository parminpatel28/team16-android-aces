package com.munchies_backend.spring_boot.services;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.time.Duration;

@Service  // Ensure it's a Spring-managed bean
public class S3Service {

    private static final Dotenv dotenv = Dotenv.load();

    private final String accessKeyId ;
    private final String secretAccessKey;
    private final String region;
    private final String bucketName;


    public S3Service() {
        accessKeyId = dotenv.get("AWS_ACCESS_KEY_ID");
        secretAccessKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
        region = dotenv.get("AWS_REGION");
        bucketName = dotenv.get("AWS_S3_BUCKET_NAME");
    }

    public String generatePresignedUrl(String fileName, String contentType) {
        if (accessKeyId == null || secretAccessKey == null || bucketName == null) {
            throw new IllegalStateException("AWS credentials or bucket name are missing.");
        }


        S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .region(Region.of(region))
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(b -> b.bucket(bucketName).key( fileName).contentType("image/*"))
                .build();

        return presigner.presignPutObject(presignRequest).url().toString();

    }
}
