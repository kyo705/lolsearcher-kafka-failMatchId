package com.lolsearcher.persistance.failmatchids.api;

import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.InfoDto;
import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.MatchDto;
import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.MetadataDto;

public class RiotGamesApiServiceUnitTestSetUp {

    protected static MatchDto getMatchData() {
        MetadataDto metadataDto = new MetadataDto();
        metadataDto.setMatchId("matchId1");
        metadataDto.setDataVersion("v1");

        InfoDto infoDto = new InfoDto();
        infoDto.setGameId(1L);
        infoDto.setQueueId(420);

        MatchDto matchDto = new MatchDto();
        matchDto.setInfo(infoDto);
        matchDto.setMetadata(metadataDto);

        return matchDto;
    }
}
