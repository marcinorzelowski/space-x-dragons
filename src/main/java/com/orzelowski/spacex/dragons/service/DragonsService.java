package com.orzelowski.spacex.dragons.service;

import com.orzelowski.spacex.dragons.enums.MissionStatus;
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

    public void createRocket(String rocketName) {

    }

    public void assingRocketToMission(UUID rocketID, UUID missionID) {

    }

    public void createMission(String missionName) {}

    public void changeMissionStatus(UUID missionID, MissionStatus missionStatus) {}

    public String getMissionsSummary() {return "";}
}
