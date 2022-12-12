package com.lolsearcher.persistance.failmatchids.api;

import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.MatchDto;
import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@RequiredArgsConstructor
@Component
public class WebClientRiotGamesApi implements RiotGamesApi {

    @Value("${app.riot_games.token_key}")
    private String key;
    private final WebClient webClient;

    @Override
    public Match getMatch(String matchId) throws WebClientResponseException {
        String uri = "/lol/match/v5/matches/" + matchId + "?api_key=" + key;

        MatchDto matchDto = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(MatchDto.class)
                .block();

        return new Match(matchDto);
    }
}
