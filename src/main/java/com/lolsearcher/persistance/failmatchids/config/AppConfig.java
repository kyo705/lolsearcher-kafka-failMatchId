package com.lolsearcher.persistance.failmatchids.config;

import com.lolsearcher.persistance.failmatchids.constant.RiotGamesConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(RiotGamesConstants.ASIA_BASE_URL)
                .build();
    }
}
