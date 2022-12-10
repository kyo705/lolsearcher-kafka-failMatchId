package com.lolsearcher.persistance.failmatchids.model.entity.match;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lolsearcher.persistance.failmatchids.model.dto.riot.match.MatchDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "MATCHES")
public class Match {

	@Id
	@Column(name = "ID")
	private String matchId;
	private long gameDuration;
	private long gameEndTimestamp;
	private int queueId;
	private int season;

	@JsonManagedReference
	@BatchSize(size = 100)
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
