package com.architechlabs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Rule {
    private String ruleId;
    private String name;
    private int priority;
    private List<Condition> conditions;
    private String logic; // AND / OR
    private List<Action> actions;
}