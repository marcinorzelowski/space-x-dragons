package com.orzelowski.spacex.dragons.model;

import com.orzelowski.spacex.dragons.enums.MissionStatus;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Mission {
    private String name;
    private MissionStatus missionStatus = MissionStatus.SCHEDULED;
    private final Set<Rocket> rockets = new HashSet<>();
}
