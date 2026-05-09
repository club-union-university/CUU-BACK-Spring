package sms.uccbackend.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class FirebaseConfig {

    /**
     * Production (Railway, etc.): set {@code FIREBASE_SERVICE_ACCOUNT_JSON} to the raw JSON body,
     * or {@code GOOGLE_APPLICATION_CREDENTIALS} to a filesystem path inside the container.
     * Local: classpath {@code firebase-service-account.json} when present.
     */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        try (InputStream serviceAccount = credentialsStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp app = FirebaseApp.initializeApp(options);
            System.out.println("[FirebaseConfig] FirebaseApp initialized.");
            return app;
        }
    }

    private static InputStream credentialsStream() throws IOException {
        String inline = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");
        if (inline != null && !inline.isBlank()) {
            return new ByteArrayInputStream(inline.strip().getBytes(StandardCharsets.UTF_8));
        }

        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            Path path = Path.of(credentialsPath);
            if (!Files.isRegularFile(path)) {
                throw new IOException(
                        "GOOGLE_APPLICATION_CREDENTIALS is set but file not found: " + credentialsPath);
            }
            return Files.newInputStream(path);
        }

        ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
        if (!resource.exists()) {
            throw new IOException("""
                    Firebase credentials not found. For cloud deploy set one of:
                    FIREBASE_SERVICE_ACCOUNT_JSON (full service account JSON)
                    GOOGLE_APPLICATION_CREDENTIALS (path to JSON file)
                    Locally add src/main/resources/firebase-service-account.json.""");
        }
        return resource.getInputStream();
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
