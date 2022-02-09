package com.spd.trello.service;

import com.spd.trello.domain.Resource;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.CommonRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class AbstractService<E extends Resource, R extends CommonRepository<E>> implements CommonService<E> {

    R repository;

    public AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public E create(E entity) {
        entity.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        return repository.save(entity);
    }

    @Override
    public E update(E entity) {
        readById(entity.getId());
        entity.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        return repository.save(entity);
    }

    @Override
    public E readById(UUID id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void delete(UUID id) {
    readById(id);
    repository.deleteById(id);
    }

    @Override
    public List<E> readAll() {
        return repository.findAll();
    }
}
