package com.wvega.barberproapi.repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FireBaseInitializer {

    @Value("${firebase.url}")
    private String firebaseUrl;

    @PostConstruct
    private void initFirestore() throws IOException {
        InputStream serviceAccount;
        serviceAccount = getClass().getClassLoader().getResourceAsStream("./firebase-credentials.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(firebaseUrl)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    public Firestore getFireStore() {
        return FirestoreClient.getFirestore();
    }

}
