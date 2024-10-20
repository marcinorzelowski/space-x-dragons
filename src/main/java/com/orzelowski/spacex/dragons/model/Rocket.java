package com.orzelowski.spacex.dragons.model;

import com.orzelowski.spacex.dragons.enums.RocketStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class Rocket {
    private String name;
    private RocketStatus status = RocketStatus.ON_GROUND;
    private Mission mission;

    public Rocket(String name) {
        this.name = name;
    }

}
