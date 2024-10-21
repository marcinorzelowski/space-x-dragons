package com.orzelowski.spacex.dragons.service;

import com.orzelowski.spacex.dragons.enums.MissionStatus;
import com.orzelowski.spacex.dragons.enums.RocketStatus;
import com.orzelowski.spacex.dragons.model.Mission;
import com.orzelowski.spacex.dragons.model.Rocket;
import com.orzelowski.spacex.dragons.repository.MissionRepository;
import com.orzelowski.spacex.dragons.repository.RocketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DragonsServiceTest {

    private RocketRepository rocketRepository;
    private MissionRepository missionRepository;
    private DragonsService dragonsService;

    @BeforeEach
    void setUp() {
        rocketRepository = new RocketRepository();
        missionRepository = new MissionRepository();
        dragonsService = new DragonsService(missionRepository, rocketRepository);
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void createRocket_InvalidString_ShouldThrowException(String name) {
        //Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> dragonsService.createRocket(name));
    }

    @Test
    void createRocket_ValidName_ShouldReturnRocketWithStatusOnGroundAndCorrectName() {
        //Arrange
        String name = "Rocket A";

        //Act
        Rocket rocket = dragonsService.createRocket(name);

        //Assert
        Assertions.assertAll(() -> {
            assertEquals(name, rocket.getName());
            assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
        });
    }

    @ParameterizedTest
    @CsvSource({
            "'', ''",           // Both empty strings
            "null, ''",         // First null, second empty
            "'', null",         // First empty, second null
            "null, null",       // Both null
            "'text', ' '",       // First non-empty, second whitespace
            "' ', 'text'",       // First whitespace, second non-empty
    })
    void assignRocketToMission_InvalidArguments_ShouldThrowException(String rocketName, String missionName) {
        //Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> dragonsService.assignRocketToMission(rocketName, missionName));
    }

    @Test
    void assignRocketToMission_RocketWithGivenNameNotExist_ShouldThrowException() {
        //Arrange
        rocketRepository.save(new Rocket("Rocket A"));

        //Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> dragonsService.assignRocketToMission("Rocket A", "Some mission"));
    }

    @Test
    void assignRocketToMission_MissionWithGivenNameNotExist_ShouldThrowException() {
        //Arrange
        missionRepository.save(new Mission("Mission A"));

        //Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> dragonsService.assignRocketToMission("Rocket A", "Mission A"));
    }

    @Test
    void assignRocketToMission_ValidRocketNameAndMissionName_ShouldUpdateRocketAndMissionData() {
        //Arrange
        missionRepository.save(new Mission("Mission A"));
        rocketRepository.save(new Rocket("Rocket A"));

        //Act
        dragonsService.assignRocketToMission("Rocket A", "Some mission");

        Mission mission = missionRepository.findByName("Mission A").get();
        Rocket rocket = rocketRepository.findByName("Rocket A").get();

        //Assert
        Assertions.assertAll(() -> {
            assertTrue(mission.getRockets().contains(rocket));
            assertEquals(rocket.getMission(), mission);
            assertEquals(MissionStatus.IN_PROGRESS, mission.getMissionStatus());
            assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void createMission_InvalidInput_ShouldThrowException(String name) {
        //Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> dragonsService.createMission(name));
    }

    @Test
    void createMission_ValidName_ShouldReturnMissionWithStatusScheduledAndCorrectName() {
        //Arrange
        String missionName = "Mission A";

        //Act
        Mission mission = dragonsService.createMission(missionName);

        Assertions.assertAll(() -> {
            assertEquals(missionName, mission.getName());
            assertEquals(MissionStatus.SCHEDULED, mission.getMissionStatus());
        });
    }

    @Test
    void changeMissionStatus_MissionWithGivenNameNotExist_ShouldThrowException() {
        //Arrange
        String missionName = "Mission A";

        //Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> dragonsService.changeMissionStatus(missionName, MissionStatus.SCHEDULED));
    }

    @Test
    void changeMissionStatus_ToEnded_AssignedRocketsShouldNotBeAssignedAndChangedToOnGroundStatus() {
        //Arrange
        Mission mission = new Mission("Mission A");
        mission.setMissionStatus(MissionStatus.IN_PROGRESS);
        Rocket rocket = new Rocket("Rocket A");
        rocket.setStatus(RocketStatus.IN_SPACE);
        rocket.setMission(mission);
        mission.getRockets().add(rocket);

        missionRepository.save(mission);
        rocketRepository.save(rocket);

        //Act
        dragonsService.changeMissionStatus(mission.getName(), MissionStatus.ENDED);

        Mission updatedMission = missionRepository.findByName("Mission A").get();
        Rocket updatedRocket = rocketRepository.findByName("Rocket A").get();

        //Assert
        Assertions.assertAll(() -> {
            assertEquals(MissionStatus.ENDED, updatedMission.getMissionStatus());
            assertEquals(updatedRocket.getStatus(), RocketStatus.ON_GROUND);
            assertNull(updatedRocket.getMission());
            assertEquals(0, updatedMission.getRockets().size());
        });
    }

    @Test
    void getMissionsSummary_MissionWithoutRockets_ShouldReturnMissionsSummary() {
        //Arrange
        missionRepository.save(new Mission("Mission A"));

        StringBuilder sb = new StringBuilder("Mission A - SCHEDULED - Dragons: 0");
        sb.append("\n");

        //Act
        String summary = dragonsService.getMissionsSummary();

        //Assert
        Assertions.assertAll(() -> {
            assertEquals(sb.toString(), summary);
        });
    }

    @Test
    void getMissionsSummary_MissionWithRocket_ShouldReturnMissionsSummaryWithRockets() {
        //Arrange
        missionRepository.save(new Mission("Mission A"));
        rocketRepository.save(new Rocket("Rocket A"));
        dragonsService.assignRocketToMission("Rocket A", "Mission A");

        StringBuilder sb = new StringBuilder("Mission A - IN_PROGRESS - Dragons: 1");
        sb.append("\n");
        sb.append("Rocket A - ON_GROUND");
        sb.append("\n");

        //Act
        String summary = dragonsService.getMissionsSummary();

        //Assert
        Assertions.assertAll(() -> {
            assertEquals(sb.toString(), summary);
        });
    }

}