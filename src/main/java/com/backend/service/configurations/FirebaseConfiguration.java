package com.backend.service.configurations;

import com.backend.service.Application;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfiguration {
  @Value("${firebase.json}")
  private String firebaseJson;
  @Value("${firebase.storage.bucket.name}")
  private String bucketName;

  @PostConstruct
  public void initializeFirebaseApp() throws IOException {
    InputStream inputStream = Application.class.getResourceAsStream(firebaseJson);

    if (inputStream == null) {
      throw new IOException("Not found file `" + firebaseJson + "`");
    }

    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(inputStream))
        .build();

    FirebaseApp.initializeApp(options);
  }
}
