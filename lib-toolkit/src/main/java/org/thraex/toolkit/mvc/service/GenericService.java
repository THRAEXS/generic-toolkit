package org.thraex.toolkit.mvc.service;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.thraex.toolkit.entity.JpaEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author 鬼王
 * @date 2022/04/06 22:32
 */
public interface GenericService <T extends JpaEntity<T>, R extends JpaRepositoryImplementation<T, String>> {

    String[] IGNORED_PATHS_UPDATE = {
            "id",
            "deleted",
            "createdBy",
            "createdDate",
            "modifiedBy",
            "modifiedDate"
    };

    String[] IGNORED_PATHS_FIND = { "enabled", "deleted" };

    R repo();

    /**
     * Returns a single entity matching the given {@link Example} or {@literal null} if none was found.
     * e.g.: a = ? or b = ?
     *
     * @param probe must not be {@literal null}.
     * @param ignoredPaths must not be {@literal null} and not empty.
     * @return a single entity matching the given {@link Example} or {@link Optional#empty()} if none was found.
     */
    Optional<T> findOneByAny(T probe, String... ignoredPaths);

    /**
     * Saves a given entity.
     * {@code <S extends T>}
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    T save(T entity, String... ignoredPaths);

    <S extends T> List<S> saveAll(Iterable<S> entities);

}
