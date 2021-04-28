package com.mitrais.atm_simulation.model;
import java.time.LocalDateTime;
public class AccountTransaction {
	private String id;
	private String type;
	private LocalDateTime transcactionDatetime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LocalDateTime getTranscactionDatetime() {
		return transcactionDatetime;
	}
	public void setTranscactionDatetime(LocalDateTime transcactionDatetime) {
		this.transcactionDatetime = transcactionDatetime;
	}

}
