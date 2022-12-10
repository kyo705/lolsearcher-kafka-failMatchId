package com.lolsearcher.persistance.failmatchids.model.dto.riot.match.perk;

import lombok.Data;

import java.util.List;

@Data
public class PerksDto {
	private final PerkStatsDto statPerks;
	private final List<PerkStyleDto> styles;
}
