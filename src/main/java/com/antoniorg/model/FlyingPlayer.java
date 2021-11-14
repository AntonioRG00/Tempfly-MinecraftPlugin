package com.antoniorg.model;

import java.util.Date;

public class FlyingPlayer {

	private String playerName;
	private Date startTime;
	
	public FlyingPlayer(String playerName, Date startTime) {
		this.playerName = playerName;
		this.startTime = startTime;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "FlyingPlayer [playerName=" + playerName + ", startTime=" + startTime + "]";
	}
}
