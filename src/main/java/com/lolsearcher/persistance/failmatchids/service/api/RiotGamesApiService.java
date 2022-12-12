package com.lolsearcher.persistance.failmatchids.service.api;

import com.lolsearcher.persistance.failmatchids.api.RiotGamesApi;
import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class RiotGamesApiService {
    private final RiotGamesApi riotGamesApi;

    public Match requestMatch(String matchId) {
        return riotGamesApi.getMatch(matchId);
    }
}
