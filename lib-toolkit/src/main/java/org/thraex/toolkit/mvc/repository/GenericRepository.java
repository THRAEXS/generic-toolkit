package org.thraex.toolkit.mvc.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * TODO: feat
 *
 * @author 鬼王
 * @date 2022/03/17 20:21
 */
@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends JpaRepositoryImplementation<T, ID> {
}
