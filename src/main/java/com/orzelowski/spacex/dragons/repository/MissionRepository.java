package com.orzelowski.spacex.dragons.repository;


import com.orzelowski.spacex.dragons.model.Mission;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MissionRepository implements LocalRepository<Mission, UUID> {
    private final Map<UUID, Mission> data = new HashMap<>();

    @Override
    public List<Mission> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public Optional<Mission> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Mission save(Mission entity) {
        if (entity.getId() != null) {
            data.put(entity.getId(), entity);
        } else {
            entity.setId(UUID.randomUUID());
            data.put(entity.getId(), entity);;
        }
        return entity;
    }

    @Override
    public void delete(Mission entity) {
        data.remove(entity.getId());
    }
}
