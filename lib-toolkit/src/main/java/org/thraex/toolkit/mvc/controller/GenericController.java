package org.thraex.toolkit.mvc.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thraex.toolkit.entity.JpaEntity;
import org.thraex.toolkit.mvc.service.GenericService;

/**
 * @author 鬼王
 * @date 2022/03/18 13:51
 */
public abstract class GenericController<T extends JpaEntity<T>, S extends GenericService<T, ?>> {

    @Autowired
    protected S service;

    @GetMapping("{id}")
    public T one(@PathVariable String id) {
        return service.repo().findById(id).orElseThrow(() ->
                new EmptyResultDataAccessException(String.format("No entity with id %s exists!", id), 1));
    }

    @PostMapping
    public T save(@Valid @RequestBody T entity) {
        return service.save(entity);
    }

    @PutMapping
    public T update(@Valid @RequestBody T entity) {
        Assert.notNull(entity.getId(), "The given entity id must not be null!");
        return save(entity);
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable String id) {
        service.repo().deleteById(id);
        return true;
    }

}
