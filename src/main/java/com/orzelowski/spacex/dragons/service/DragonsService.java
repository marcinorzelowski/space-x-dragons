package com.orzelowski.spacex.dragons.service;

import com.orzelowski.spacex.dragons.enums.MissionStatus;
import com.orzelowski.spacex.dragons.enums.RocketStatus;
import com.orzelowski.spacex.dragons.model.Mission;
import com.orzelowski.spacex.dragons.model.Rocket;
import com.orzelowski.spacex.dragons.repository.MissionRepository;
import com.orzelowski.spacex.dragons.repository.RocketRepository;
import com.orzelowski.spacex.dragons.util.MissionComparator;
import com.orzelowski.spacex.dragons.util.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DragonsService {

    private final MissionRepository missionRepository;
    private final RocketRepository rocketRepository;

    public DragonsService(MissionRepository missionRepository, RocketRepository rocketRepository) {
        this.missionRepository = missionRepository;
        this.rocketRepository = rocketRepository;
    }

    public Rocket createRocket(String rocketName) {
        ValidationUtils.validateString(rocketName);
        return rocketRepository.save(new Rocket(rocketName));
    }

    public void assignRocketToMission(String rocketName, String missionName) {
        Rocket rocket = rocketRepository.findByName(rocketName).orElseThrow(IllegalArgumentException::new);
        Mission mission = missionRepository.findByName(missionName).orElseThrow(IllegalArgumentException::new);

        if (rocket.getStatus().equals(RocketStatus.ON_GROUND)) {
            mission.getRockets().add(rocket);
            if (mission.getMissionStatus().equals(MissionStatus.SCHEDULED))
                mission.setMissionStatus(MissionStatus.IN_PROGRESS);
            rocket.setMission(mission);
            rocket.setStatus(RocketStatus.IN_SPACE);
            rocketRepository.save(rocket);
            missionRepository.save(mission);
        } else {
            throw new IllegalArgumentException("Rocket status is not ON GROUND");
        }

    }

    public void changeRocketStatus(String rocketName, RocketStatus rocketStatus) {
        Rocket rocket = rocketRepository.findByName(rocketName).orElseThrow(IllegalArgumentException::new);
        Mission mission = rocket.getMission();
        switch (rocketStatus) {
            case ON_GROUND:
                rocket.setStatus(rocketStatus);
                if (mission != null) {
                    mission.getRockets().remove(rocket);
                    if (mission.getRockets().isEmpty())
                        mission.setMissionStatus(MissionStatus.SCHEDULED);
                }
                break;
            case IN_REPAIR:
                rocket.setStatus(rocketStatus);
                if (mission != null) {
                    mission.setMissionStatus(MissionStatus.PENDING);
                }
            case IN_SPACE:
                throw new IllegalArgumentException("Rocket status is IN SPACE is not supported");
            default:
                throw new IllegalArgumentException("Rocket status is not supported");
        }

    }

    public Mission createMission(String missionName) {
        ValidationUtils.validateString(missionName);
        return missionRepository.save(new Mission(missionName));
    }

    public void changeMissionStatus(String missionName, MissionStatus missionStatus) {
        Mission mission = missionRepository.findByName(missionName).orElseThrow(IllegalArgumentException::new);

        switch (missionStatus) {
            case SCHEDULED, ENDED:
                mission.setMissionStatus(missionStatus);
                mission.getRockets().forEach(rocket -> {
                    rocket.setMission(null);
                    rocket.setStatus(RocketStatus.ON_GROUND);
                    rocketRepository.save(rocket);
                });
                mission.getRockets().clear();
                missionRepository.save(mission);
                break;
                case PENDING:
                    throw new IllegalArgumentException("Pending status not supported");
            default:
                throw new IllegalArgumentException("Unknown mission status");
        }
    }

    public String getMissionsSummary() {
        StringBuilder sb = new StringBuilder();
        List<Mission> missions = missionRepository.findAll();
        Comparator<Mission> missionComparator = new MissionComparator();
        missions.sort(missionComparator);
        for (Mission mission : missions) {
            sb.append(String.format("%s - %s - Dragons: %d", mission.getName(), mission.getMissionStatus().name(), mission.getRockets().size()));
            sb.append("\n");
            for (Rocket rocket : mission.getRockets()) {
                sb.append(String.format("%s - %s", rocket.getName(), rocket.getStatus().name()));
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
