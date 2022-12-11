package com.lolsearcher.persistance.failmatchids.api;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;

import java.util.List;

public interface RiotGamesApi {
    List<Match> getMatches(List<String> matchIds);
}
