package com.lolsearcher.persistance.failmatchids.model.dto.riot.match;

import lombok.Data;

import java.util.List;

@Data
public class MetadataDto {
    private String dataVersion;
    private String matchId;
    private List<String> participants;
}
