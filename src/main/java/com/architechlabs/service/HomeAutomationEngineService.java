package com.architechlabs.service;

import com.architechlabs.model.*;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class HomeAutomationEngineService {

    private Map<String, Device> devices = new HashMap<>();
    private List<Rule> rules = new ArrayList<>();
    private Map<String, Deque<StateHistory>> history = new HashMap<>();

    // ADD DEVICE
    public void addDevice(Device device) {

        devices.put(device.getDeviceId(), device);

        Deque<StateHistory> dq = new LinkedList<>();

        dq.addLast(new StateHistory(
                LocalDateTime.now(),
                device.getCurrentState()
        ));

        history.put(device.getDeviceId(), dq);

        System.out.println("Device added: " + device.getDeviceId());
    }

    // UPDATE DEVICE
    public void updateDevice(String deviceId, Map<String, Object> newState) {

        Device device = devices.get(deviceId);

        if (device == null) {
            System.out.println("Device not found");
            return;
        }

        device.setCurrentState(newState);
        device.setLastUpdate(LocalDateTime.now());

        history.get(deviceId).addLast(
                new StateHistory(LocalDateTime.now(), newState)
        );

        evaluateRules();

        System.out.println("Device updated: " + deviceId);
    }

    // ADD RULE
    public void addRule(Rule rule) {
        rules.add(rule);
        System.out.println("Rule added: " + rule.getName());
    }

    // RULE ENGINE
    private void evaluateRules() {

        Map<String, Action> finalActions = new HashMap<>();
        Map<String, Integer> priorityMap = new HashMap<>();

        for (Rule rule : rules) {

            if (!evaluateRule(rule)) continue;

            for (Action action : rule.getActions()) {

                String key = action.getDeviceId() + action.getAction();

                if (!finalActions.containsKey(key)) {
                    finalActions.put(key, action);
                    priorityMap.put(key, rule.getPriority());
                } else {

                    if (rule.getPriority() > priorityMap.get(key)) {
                        finalActions.put(key, action);
                        priorityMap.put(key, rule.getPriority());
                    }
                }
            }
        }

        for (Action action : finalActions.values()) {
            execute(action);
        }
    }

    private boolean evaluateRule(Rule rule) {

        boolean result = rule.getLogic().equals("AND");

        for (Condition c : rule.getConditions()) {

            Device d = devices.get(c.getDeviceId());

            if (d == null) return false;

            Object actual = d.getCurrentState().get(c.getKey());

            boolean conditionResult = actual != null && actual.equals(c.getValue());

            if (rule.getLogic().equals("AND")) {
                result = result && conditionResult;
            } else {
                result = result || conditionResult;
            }
        }

        return result;
    }

    private void execute(Action action) {

        Device device = devices.get(action.getDeviceId());

        if (device != null && action.getAction().equals("set")) {
            device.setCurrentState(action.getParams());
        }

        System.out.println("Executed action on: " + action.getDeviceId());
    }

    // STATES
    public Map<String, Object> getAllDeviceStates() {
        Map<String, Object> map = new HashMap<>();
        for (Device d : devices.values()) {
            map.put(d.getDeviceId(), d.getCurrentState());
        }
        return map;
    }

    // HISTORY
    public List<StateHistory> getDeviceHistory(String id, int limit) {

        Deque<StateHistory> dq = history.get(id);

        if (dq == null) return new ArrayList<>();

        List<StateHistory> list = new ArrayList<>(dq);

        return list.subList(
                Math.max(0, list.size() - limit),
                list.size()
        );
    }
}