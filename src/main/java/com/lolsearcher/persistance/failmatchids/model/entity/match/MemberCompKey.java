package com.lolsearcher.persistance.failmatchids.model.entity.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberCompKey implements Serializable {
	private static final long serialVersionUID = 3555370525672043186L;

	private String matchId;
	private int num;
}
