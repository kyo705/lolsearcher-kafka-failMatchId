package com.lolsearcher.persistance.failmatchids.model.dto.riot.match.perk;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerkStatsDto {
	private Short defense;
	private Short flex;
	private Short offense;
}
