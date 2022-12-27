package com.lolsearcher.persistance.failmatchids.scheduler.info;

import lombok.Data;

import java.io.Serializable;

@Data
public class Timer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int totalFireCount;
	private boolean runForever;
	private long repeatIntervalMs;
	private long initialOffsetMs;
	private String callbackData;
	
	public Timer() {
		//default
		totalFireCount = 1;
		runForever = false;
		repeatIntervalMs = 1000;
		initialOffsetMs = 0;
		callbackData = null;
	}
}
