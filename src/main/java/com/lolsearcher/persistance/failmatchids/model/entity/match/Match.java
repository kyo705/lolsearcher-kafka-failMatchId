package com.lolsearcher.persistance.failmatchids.model.entity.match;

import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.MatchDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class Match {

	private String matchId;
	private long gameDuration;
	private long gameEndTimestamp;
	private int queueId;
	private int season;

	private List<Member> members = new ArrayList<>(10);

	public Match(MatchDto matchDto){
		this.matchId = matchDto.getMetadata().getMatchId();
		this.gameDuration = matchDto.getInfo().getGameDuration();
		this.gameEndTimestamp = matchDto.getInfo().getGameEndTimestamp();
		this.queueId = matchDto.getInfo().getQueueId();

		this.members = new ArrayList<>();
	}

	public void addMember(Member member) {
		this.members.add(member);
	}
	public void removeMember(Member member) {
		this.members.remove(member);
	}
}
