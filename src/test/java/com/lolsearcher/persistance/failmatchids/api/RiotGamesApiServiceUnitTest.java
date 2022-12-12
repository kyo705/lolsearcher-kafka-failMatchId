package com.lolsearcher.persistance.failmatchids.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.MatchDto;
import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class RiotGamesApiServiceUnitTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MockWebServer mockWebServer;
    private WebClientRiotGamesApi webClientRiotGamesApi;

    @BeforeEach
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        webClientRiotGamesApi = new WebClientRiotGamesApi(
                WebClient.builder()
                        .baseUrl(baseUrl)
                        .build()
        );
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DisplayName("올바른 값으로 게임 서버에 API 요청한 경우 200 OK가 응답된다.")
    @Test
    void requestMatchDataWithSuccess() throws JsonProcessingException, InterruptedException {
        //given
        String matchId = "matchId1";

        MatchDto matchData = RiotGamesApiServiceUnitTestSetUp.getMatchData();

        mockWebServer.enqueue(
                new MockResponse()
                        .setHeader("Content-Type", "application/json")
                        .setBody(objectMapper.writeValueAsString(matchData))
        );

        //when
        Match result = webClientRiotGamesApi.getMatch(matchId);

        //then
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        assertThat(recordedRequest.getPath()).isEqualTo("/lol/match/v5/matches/matchId1?api_key=null");

        assertThat(result.getMatchId()).isEqualTo(matchData.getMetadata().getMatchId());
        assertThat(result.getQueueId()).isEqualTo(matchData.getInfo().getQueueId());
    }

    @DisplayName("올바르지 못한 API 요청일 경우 WebClientResponseException이 발생한다.")
    @Test
    void requestMatchDataWithWebClientResponseException() {
        //given
        String matchId = "matchId1";

        mockWebServer.enqueue(
                new MockResponse()
                        .setHeader("Content-Type", "application/json")
                        .setResponseCode(HttpStatus.TOO_MANY_REQUESTS.value())
        );

        //when & then
        WebClientResponseException e = assertThrows(WebClientResponseException.class,
                ()->webClientRiotGamesApi.getMatch(matchId));
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }
}
