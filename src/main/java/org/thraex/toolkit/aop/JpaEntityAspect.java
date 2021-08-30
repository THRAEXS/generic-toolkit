package org.thraex.toolkit.aop;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thraex.toolkit.entity.JpaEntity;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * TODO:
 * <pre>
 * 统一设置创建人/创建时间/更新人/更新时间
 * 1. {@code @Pointcut("target(javax.persistence.EntityManager)")} startsWithSave
 * 2. {@code @Pointcut("target(org.springframework.data.repository.Repository)")}: persist/merge
 * 3. {@code @PrePersist} / {@code @PreUpdate}: by???
 * 4. {@code @EntityListeners(AuditingEntityListener.class)}: {@code @EnableJpaAuditing} / {@code @CreatedBy} / {@code @CreatedDate} ...
 * </pre>
 * @deprecated See {@link JpaEntity#prePersist()} and {@link JpaEntity#preUpdate()}
 * @author 鬼王
 * @date 2021/08/24 17:04
 */
@Deprecated
@Aspect
public class JpaEntityAspect {

    protected static final Logger logger =  LoggerFactory.getLogger(JpaEntityAspect.class);

    private static final String METHOD_SAVE = "save";

    @Pointcut("target(org.springframework.data.repository.Repository)")
    public void points() {}

    @Before("points()")
    public void before(JoinPoint point) {
        debug(() -> logger.debug(point.toString()));

        if (startsWithSave(point.getSignature().getName())) {
            execute(point.getArgs());
        }
    }

    private void execute(Object[] args) {
        Object[] parameters = ArrayUtils.nullToEmpty(args);
        debug(() -> logger.debug("Number of parameters: {}", parameters.length));

        Stream.of(parameters).forEach(it -> {
            JpaEntity e = (JpaEntity) it;
            if (StringUtils.isBlank(e.getId())) {
                e.setCreatedBy("CREATE-BY").setModifiedDate(LocalDateTime.now());
            } else {
                e.setModifiedBy("UPDATE-BY").setModifiedDate(LocalDateTime.now());
            }
        });
    }

    private boolean startsWithSave(String methodName) {
        return StringUtils.startsWithIgnoreCase(methodName, METHOD_SAVE);
    }

    private void debug(Runnable print) {
        if (logger.isDebugEnabled()) { print.run(); }
    }

}
