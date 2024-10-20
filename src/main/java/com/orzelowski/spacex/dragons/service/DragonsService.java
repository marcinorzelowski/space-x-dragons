package com.orzelowski.spacex.dragons.service;

import com.orzelowski.spacex.dragons.enums.MissionStatus;
import com.orzelowski.spacex.dragons.model.Mission;
import com.orzelowski.spacex.dragons.model.Rocket;
import com.orzelowski.spacex.dragons.repository.MissionRepository;
import com.orzelowski.spacex.dragons.repository.RocketRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DragonsService {

    private final MissionRepository missionRepository;
    private final RocketRepository rocketRepository;

    public DragonsService(MissionRepository missionRepository, RocketRepository rocketRepository) {
        this.missionRepository = missionRepository;
        this.rocketRepository = rocketRepository;
    }

    public Rocket createRocket(String rocketName) {
      return null;
    }

    public void assignRocketToMission(String rocketName, String missionName) {

    }

    public void changeRocketStatus(String rocketName, String rocketStatus) {}

    public Mission createMission(String missionName) {
        return null;
    }

    public void changeMissionStatus(String missionName, MissionStatus missionStatus) {}

    public String getMissionsSummary() {return "";}
}
