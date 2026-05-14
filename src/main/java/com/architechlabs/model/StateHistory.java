package com.architechlabs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class StateHistory {
    private LocalDateTime timestamp;
    private Map<String, Object> state;
}