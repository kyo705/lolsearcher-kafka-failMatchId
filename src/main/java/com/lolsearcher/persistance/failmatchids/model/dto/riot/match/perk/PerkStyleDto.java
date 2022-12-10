package com.lolsearcher.persistance.failmatchids.model.dto.riot.match.perk;

import lombok.Data;

import java.util.List;

@Data
public class PerkStyleDto {
	private final String description;
	private final List<PerkStyleSelectionDto> selections;
	private final short style;
}
