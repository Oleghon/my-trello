package com.spd.trello.controller;

import com.spd.trello.domain.Resource;
import com.spd.trello.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
@Slf4j
public class AbstractController<E extends Resource, S extends CommonService<E>>
        implements IController<E> {

    S service;

    public AbstractController(S service) {
        this.service = service;
    }

    @PostMapping
    @Override
    public ResponseEntity<E> create(@RequestBody @Valid E resource) {
        E result = service.create(resource);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<E> update(@PathVariable UUID id, @RequestBody @Valid E resource) {
        resource.setId(id);
        E result = service.update(resource);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<E> readById(@PathVariable UUID id) {
        E result = service.readById(id);
        log.info("entity with id: {} has just been read from db", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<E> delete(@PathVariable UUID id) {
        E result = service.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    @Override
    public Page<E> readAll(Pageable pageable) {
        return service.readAll(pageable);
    }
}
