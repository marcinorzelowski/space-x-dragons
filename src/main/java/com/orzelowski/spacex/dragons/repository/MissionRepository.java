package com.orzelowski.spacex.dragons.repository;


import com.orzelowski.spacex.dragons.model.Mission;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MissionRepository implements LocalRepository<Mission, String> {
    private final Map<String, Mission> data = new HashMap<>();

    @Override
    public List<Mission> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<Mission> findByName(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Mission save(Mission entity) {
        data.put(entity.getName(), entity);
        return entity;
    }

    @Override
    public void delete(Mission entity) {
        data.remove(entity.getName());
    }
}
