package com.lolsearcher.persistance.failmatchids.model.entity.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberCompKey implements Serializable {
	private static final long serialVersionUID = 3555370525672043186L;
	
	@Column(name = "MATCH_ID")
	private String matchId;
	private int num;
}
