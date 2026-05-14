package com.architechlabs.model;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class Device {
	
	private String deviceId;
	private String name;
	private String type;
	private Map<String, Object> currentState;
	private LocalDateTime lastUpdate;

}
