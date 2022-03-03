package com.backend.service.services.firebase;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.UUID;

@Service
public class FirebaseStorageService {
  @Value("${firebase.storage.bucket.name}")
  private String bucketName;

  private Bucket bucket;

  @PostConstruct
  public void initializeBucket() {
    bucket = StorageClient.getInstance().bucket(bucketName);
  }

  public String uploadImage(byte[] buffer, UUID userId) {
    // get timestamp as number format
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long time = timestamp.getTime();

    // set blob file name
    String blob = String.format("%s/%s.jpg", userId, time);

    // upload image to firebase storage
    bucket.create(blob, buffer, "image/jpg");

    return blob;
  }

  public byte[] downloadImage(String filePath) {
    Blob blob = bucket.get(filePath);

    return blob.getContent();
  }

  public String downloadImageAsBase64(String filepath) {
    byte[] bytes = downloadImage(filepath);

    return String.format(
        "data:image/jpg;base64,%s",
        new String(Base64.getEncoder().encode(bytes)));
  }

  public boolean deleteImage(String filepath) {
    Blob blob = bucket.get(filepath);

    return blob.delete();
  }
}
