package com.orzelowski.spacex.dragons.repository;

import com.orzelowski.spacex.dragons.model.Rocket;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RocketRepository implements LocalRepository<Rocket, UUID> {
    private final HashMap<UUID, Rocket> data = new HashMap<>();
    @Override
    public List<Rocket> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public Optional<Rocket> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Rocket save(Rocket entity) {
        if (entity.getId() != null) {
            data.put(entity.getId(), entity);
        } else {
            entity.setId(UUID.randomUUID());
            data.put(entity.getId(), entity);
        }
        return entity;
    }

    @Override
    public void delete(Rocket entity) {
        data.remove(entity.getId());
    }
}
