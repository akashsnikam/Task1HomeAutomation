package com.architechlabs.runner;

import com.architechlabs.model.Action;
import com.architechlabs.model.Condition;
import com.architechlabs.model.Device;
import com.architechlabs.model.Rule;
import com.architechlabs.service.HomeAutomationEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class TestRunner implements CommandLineRunner {

    @Autowired
    private HomeAutomationEngineService engine;

    @Override
    public void run(String... args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== HOME AUTOMATION SYSTEM =====");

        while (true) {

            System.out.println("\n1. Add Device");
            System.out.println("2. Update Device");
            System.out.println("3. Add Rule (skip simple)");
            System.out.println("4. Get States");
            System.out.println("5. Get History");
            System.out.println("6. Exit");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1: {
                    System.out.print("ID: ");
                    String id = sc.nextLine();

                    System.out.print("Name: ");
                    String name = sc.nextLine();

                    System.out.print("Type: ");
                    String type = sc.nextLine();

                    System.out.print("State (key=value,key=value): ");
                    String input = sc.nextLine();

                    Map<String, Object> state = new HashMap<>();

                    for (String s : input.split(",")) {
                        String[] kv = s.split("=");
                        if (kv.length == 2)
                            state.put(kv[0], kv[1]);
                    }

                    engine.addDevice(new Device(id, name, type, state, LocalDateTime.now()));
                    break;
                }

                case 2: {
                    System.out.print("ID: ");
                    String id = sc.nextLine();

                    System.out.print("State: ");
                    String input = sc.nextLine();

                    Map<String, Object> state = new HashMap<>();

                    for (String s : input.split(",")) {
                        String[] kv = s.split("=");
                        if (kv.length == 2)
                            state.put(kv[0], kv[1]);
                    }

                    engine.updateDevice(id, state);
                    break;
                }

                case 3: {

                    System.out.println("\n===== ADD RULE MENU =====");
                    System.out.println("1. Motion → Light ON Rule");
                    System.out.println("2. Motion → Light OFF Rule");
                    System.out.println("3. Brightness Rule");
                    System.out.print("Enter choice: ");

                    int ruleChoice = Integer.parseInt(sc.nextLine());

                    switch (ruleChoice) {

                        // 🔥 RULE 1
                        case 1: {
                            Rule motionOnRule = new Rule(
                                    "r1",
                                    "Motion ON Light",
                                    10,
                                    List.of(
                                            new Condition("motion1", "motion", "==", "true")
                                    ),
                                    "AND",
                                    List.of(
                                            new Action("light1", "set",
                                                    Map.of("power", "on", "brightness", "100"))
                                    )
                            );

                            engine.addRule(motionOnRule);
                            System.out.println("Rule Added: Motion → Light ON");
                            break;
                        }

                        // 🔥 RULE 2
                        case 2: {
                            Rule motionOffRule = new Rule(
                                    "r2",
                                    "Motion OFF Light",
                                    9,
                                    List.of(
                                            new Condition("motion1", "motion", "==", "false")
                                    ),
                                    "AND",
                                    List.of(
                                            new Action("light1", "set",
                                                    Map.of("power", "off", "brightness", "0"))
                                    )
                            );

                            engine.addRule(motionOffRule);
                            System.out.println("Rule Added: Motion → Light OFF");
                            break;
                        }

                        // 🔥 RULE 3
                        case 3: {
                            Rule brightnessRule = new Rule(
                                    "r3",
                                    "High Brightness on Motion",
                                    8,
                                    List.of(
                                            new Condition("motion1", "motion", "==", "true")
                                    ),
                                    "AND",
                                    List.of(
                                            new Action("light1", "set",
                                                    Map.of("power", "on", "brightness", "150"))
                                    )
                            );

                            engine.addRule(brightnessRule);
                            System.out.println("Rule Added: Brightness Boost Rule");
                            break;
                        }

                        default:
                            System.out.println("Invalid Rule Choice");
                    }

                    break;
                }

                case 4:
                    System.out.println(engine.getAllDeviceStates());
                    break;

                case 5: {
                    System.out.print("ID: ");
                    String id = sc.nextLine();

                    System.out.println(engine.getDeviceHistory(id, 10));
                    break;
                }

                case 6:
                    return;
            }
        }
    }
}