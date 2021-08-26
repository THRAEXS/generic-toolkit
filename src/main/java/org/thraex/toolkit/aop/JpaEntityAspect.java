package org.thraex.toolkit.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author 鬼王
 * @date 2021/08/24 17:04
 */
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
        debug(() -> logger.debug("before execute...."));
    }

    private boolean startsWithSave(String methodName) {
        return StringUtils.startsWithIgnoreCase(methodName, METHOD_SAVE);
    }

    private void debug(Runnable print) {
        if (logger.isDebugEnabled()) { print.run(); }
    }

}
