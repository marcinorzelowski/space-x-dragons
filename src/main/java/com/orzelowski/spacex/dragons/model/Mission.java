package com.orzelowski.spacex.dragons.model;

import com.orzelowski.spacex.dragons.enums.MissionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
public class Mission {
    private String name;
    private MissionStatus missionStatus = MissionStatus.SCHEDULED;
    @EqualsAndHashCode.Exclude
    private final Set<Rocket> rockets = new HashSet<>();

    public Mission(String name) {
        this.name = name;
    }


}
