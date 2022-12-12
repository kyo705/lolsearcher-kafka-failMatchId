package com.lolsearcher.persistance.failmatchids.api;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;


public interface RiotGamesApi {
    Match getMatch(String matchId);
}
