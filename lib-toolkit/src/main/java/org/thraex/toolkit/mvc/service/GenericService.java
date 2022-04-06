package org.thraex.toolkit.mvc.service;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.util.Assert;
import org.thraex.toolkit.entity.JpaEntity;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * TODO: feat
 *
 * @author 鬼王
 * @date 2022/03/16 18:43
 */
public class GenericService<T extends JpaEntity<T>, R extends JpaRepositoryImplementation<T, String>> {

    public static final String[] IGNORED_PATHS_UPDATE = {
            "id",
            "deleted",
            "createdBy",
            "createdDate",
            "modifiedBy",
            "modifiedDate"
    };

    public static final String[] IGNORED_PATHS_FIND = { "enabled", "deleted" };

    @Autowired
    protected R repository;

    public R repo() {
        return repository;
    }

    /**
     * Returns a single entity matching the given {@link Example} or {@literal null} if none was found.
     * e.g.: a = ? or b = ?
     *
     * @param probe must not be {@literal null}.
     * @param ignoredPaths must not be {@literal null} and not empty.
     * @return a single entity matching the given {@link Example} or {@link Optional#empty()} if none was found.
     */
    public Optional<T> findOneByAny(T probe, String... ignoredPaths) {
        String[] paths = ArrayUtils.addAll(IGNORED_PATHS_FIND, ignoredPaths);
        ExampleMatcher matcher = ExampleMatcher.matchingAny().withIgnorePaths(paths);
        Example<T> example = Example.of(probe, matcher);

        return repository.findOne(example);
    }

    /**
     * Saves a given entity.
     * {@code <S extends T>}
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    public T save(T entity, String... ignoredPaths) {
        Assert.notNull(entity, "The given entity must not be null!");

        String id = entity.getId();
        Supplier<T> from = () -> {
            T old = repository.findById(id).orElseThrow(() ->
                    new EmptyResultDataAccessException(String.format("Target does not exist: [%s]", id), 1));
            String[] ignoreProperties = ArrayUtils.addAll(IGNORED_PATHS_UPDATE, ignoredPaths);
            BeanUtils.copyProperties(entity, old, ignoreProperties);

            return old;
        };

        T edit = StringUtils.isBlank(id) ? entity : from.get();

        return repository.save(edit);
    }

}
