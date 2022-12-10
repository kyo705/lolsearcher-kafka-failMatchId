package com.lolsearcher.persistance.failmatchids.model.entity.match;

import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.perk.PerkStatsDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PerkStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Short defense;
    private Short flex;
    private Short offense;

    public PerkStats(PerkStatsDto perkStatsDto){
        this.defense = perkStatsDto.getDefense();
        this.flex = perkStatsDto.getFlex();
        this.offense = perkStatsDto.getOffense();
    }
}
