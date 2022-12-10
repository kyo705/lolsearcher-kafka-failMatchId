package com.lolsearcher.persistance.failmatchids.model.dto.riot.match.team;

import lombok.Data;

@Data
public class ObjectivesDto {
    private ObjectiveDto baron;
    private ObjectiveDto champion;
    private ObjectiveDto dragon;
    private ObjectiveDto inhibitor;
    private ObjectiveDto riftHerald;
    private ObjectiveDto tower;

}
