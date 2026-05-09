package sms.uccbackend.global.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AiClientConfig {

    @Bean
    public RestClient aiRestClient(@Value("${ai.node-server.base-url:http://localhost:3000/api}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
