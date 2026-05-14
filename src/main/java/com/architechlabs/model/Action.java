package com.architechlabs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Action {
    private String deviceId;
    private String action;
    private Map<String, Object> params;
}