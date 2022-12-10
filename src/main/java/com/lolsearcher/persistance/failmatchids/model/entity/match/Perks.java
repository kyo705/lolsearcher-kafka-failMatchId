package com.lolsearcher.persistance.failmatchids.model.entity.match;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.perk.PerksDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Perks {
    @EmbeddedId
    MemberCompKey memberCompKey;

    @Column(name = "PERK_STATS_ID")
    private int perkStatsId;

    @ManyToOne
    @JoinColumn(name = "PERK_STATS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private PerkStats perkStats;

    @JsonBackReference
    @OneToOne
    @MapsId
    @JoinColumns({
            @JoinColumn(name = "MATCH_ID", referencedColumnName = "MATCH_ID"),
            @JoinColumn(name = "NUM", referencedColumnName = "NUM")
    })
    private Member member;

    private short mainPerkStyle;
    private short subPerkStyle;
    private short mainPerk1;
    private short mainPerk1Var1;
    private short mainPerk1Var2;
    private short mainPerk1Var3;

    private short mainPerk2;
    private short mainPerk2Var1;
    private short mainPerk2Var2;
    private short mainPerk2Var3;

    private short mainPerk3;
    private short mainPerk3Var1;
    private short mainPerk3Var2;
    private short mainPerk3Var3;

    private short mainPerk4;
    private short mainPerk4Var1;
    private short mainPerk4Var2;
    private short mainPerk4Var3;

    private short subPerk1;
    private short subPerk1Var1;
    private short subPerk1Var2;
    private short subPerk1Var3;

    private short subPerk2;
    private short subPerk2Var1;
    private short subPerk2Var2;
    private short subPerk2Var3;

    public Perks(PerksDto perksDto){
        this.mainPerkStyle = perksDto.getStyles().get(0).getStyle();
        this.subPerkStyle = perksDto.getStyles().get(1).getStyle();

        this.mainPerk1 =  (perksDto.getStyles().get(0).getSelections().get(0).getPerk());
        this.mainPerk1Var1 =  (perksDto.getStyles().get(0).getSelections().get(0).getVar1());
        this.mainPerk1Var2 =  (perksDto.getStyles().get(0).getSelections().get(0).getVar2());
        this.mainPerk1Var3 =  (perksDto.getStyles().get(0).getSelections().get(0).getVar3());

        this.mainPerk2 =  (perksDto.getStyles().get(0).getSelections().get(1).getPerk());
        this.mainPerk2Var1 =  (perksDto.getStyles().get(0).getSelections().get(1).getVar1());
        this.mainPerk2Var2 =  (perksDto.getStyles().get(0).getSelections().get(1).getVar2());
        this.mainPerk2Var3 =  (perksDto.getStyles().get(0).getSelections().get(1).getVar3());

        this.mainPerk3 =  (perksDto.getStyles().get(0).getSelections().get(3).getPerk());
        this.mainPerk3Var1 =  (perksDto.getStyles().get(0).getSelections().get(2).getVar1());
        this.mainPerk3Var2 =  (perksDto.getStyles().get(0).getSelections().get(2).getVar2());
        this.mainPerk3Var3 =  (perksDto.getStyles().get(0).getSelections().get(2).getVar3());

        this.mainPerk4 =  (perksDto.getStyles().get(0).getSelections().get(3).getPerk());
        this.mainPerk4Var1 =  (perksDto.getStyles().get(0).getSelections().get(3).getVar1());
        this.mainPerk4Var2 =  (perksDto.getStyles().get(0).getSelections().get(3).getVar2());
        this.mainPerk4Var3 =  (perksDto.getStyles().get(0).getSelections().get(3).getVar3());

        this.subPerk1 =  (perksDto.getStyles().get(1).getSelections().get(0).getPerk());
        this.subPerk1Var1 =  (perksDto.getStyles().get(1).getSelections().get(0).getVar1());
        this.subPerk1Var2 =  (perksDto.getStyles().get(1).getSelections().get(0).getVar2());
        this.subPerk1Var3 =  (perksDto.getStyles().get(1).getSelections().get(0).getVar3());

        this.subPerk2 =  (perksDto.getStyles().get(1).getSelections().get(1).getPerk());
        this.subPerk2Var1 =  (perksDto.getStyles().get(1).getSelections().get(1).getVar1());
        this.subPerk2Var2 =  (perksDto.getStyles().get(1).getSelections().get(1).getVar2());
        this.subPerk2Var3 =  (perksDto.getStyles().get(1).getSelections().get(1).getVar3());
    }

    public void setMember(Member member){
        this.member = member;
        member.setPerks(this);
    }
}
