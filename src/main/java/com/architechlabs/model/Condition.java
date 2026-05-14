package com.architechlabs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Condition {
    private String deviceId;
    private String key;
    private String operator;
    private Object value;
}