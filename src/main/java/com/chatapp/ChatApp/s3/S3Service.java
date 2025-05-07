package com.chatapp.ChatApp.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    public String uploadBase64Media(String base64, String fileName) {
        String contentType = "";

        if (base64.startsWith("data:image/")) {
            contentType = base64.substring(5, base64.indexOf(";")); // Ví dụ: "image/jpeg"
        } else if (base64.startsWith("data:video/")) {
            contentType = base64.substring(5, base64.indexOf(";")); // Ví dụ: "video/mp4"
        } else {
            throw new IllegalArgumentException("Unsupported file type");
        }

        // Cắt bỏ phần đầu "data:image/jpeg;base64," hoặc "data:video/mp4;base64,"
        base64 = base64.substring(base64.indexOf(",") + 1);

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType) // <-- dùng contentType động
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, decodedBytes.length));

            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload to S3", e);
        }
    }
}
