package com.lolsearcher.persistance.failmatchids.model.dto;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SuccessMatchesAndFailMatchIds {
	private List<Match> successMatches;
	private List<String> failMatchIds;
}
