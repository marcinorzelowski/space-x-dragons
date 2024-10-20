package com.orzelowski.spacex.dragons.repository;

import com.orzelowski.spacex.dragons.model.Rocket;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class RocketRepository implements LocalRepository<Rocket, String> {
    private final HashMap<String, Rocket> data = new HashMap<>();
    @Override
    public List<Rocket> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public Optional<Rocket> findByName(String name) {
        return Optional.ofNullable(data.get(name));
    }

    @Override
    public Rocket save(Rocket entity) {
        data.put(entity.getName(), entity);
        return entity;
    }

    @Override
    public void delete(Rocket entity) {
        data.remove(entity.getName());
    }
}
