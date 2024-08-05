package com.wvega.barberproapi.repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class FireBaseInitializer {

    @Value("${firebase.url}")
    private String firebaseUrl;

    @PostConstruct
    private void initFirestore() throws IOException {
        String base64Credentials = System.getenv("FIREBASE_CREDENTIALS_BASE64");

        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(decodedBytes));

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setDatabaseUrl(firebaseUrl)
                .build();

        FirebaseApp.initializeApp(options);
    }

    public Firestore getFireStore() {
        return FirestoreClient.getFirestore();
    }

}
