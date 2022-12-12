package com.lolsearcher.persistance.failmatchids.api;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RiotGamesApiServiceIntegrationTest {

    @Autowired
    private WebClientRiotGamesApi webClientRiotGamesApi;


    @DisplayName("올바른 값으로 게임 서버에 API 요청한 경우 200 OK가 응답된다.")
    @Test
    void requestMatchDataWithSuccess() {
        //given
        String matchId = "KR_5510140331";

        //when
        Match result = webClientRiotGamesApi.getMatch(matchId);

        //then
        assertThat(result.getMatchId()).isEqualTo(matchId);
    }

    @DisplayName("올바르지 못한 API 요청일 경우 WebClientResponseException이 발생한다.")
    @Test
    void requestMatchDataWithWebClientResponseException() {
        //given
        String matchId = "matchId1";

        //when & then
        WebClientResponseException e = assertThrows(WebClientResponseException.class,
                ()->webClientRiotGamesApi.getMatch(matchId));
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
