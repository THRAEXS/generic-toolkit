package org.thraex.toolkit.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.response.ResponseStatus;
import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * TODO: Optimization global Exception
 * <br>
 * Priority is higher than {@link HandlerFunctionExceptionHandler}
 *
 * @author 鬼王
 * @date 2022/03/10 11:13
 */
@RestControllerAdvice
public class RequestMappingExceptionHandler {

    private static final Logger logger = Loggers.getLogger(HandlerFunctionExceptionHandler.class);

    @ExceptionHandler(EmptyResultDataAccessException.class)
    ResponseResult handler(EmptyResultDataAccessException e) {
        logger.warn("EmptyResultDataAccessException: {}", e.getMessage());
        return ResponseResult.fail(ResponseStatus.TARGET_NOT_EXIST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseResult handler(IllegalArgumentException e) {
        logger.warn("IllegalArgumentException: {}", e.getMessage());
        return ResponseResult.fail(ResponseStatus.ILLEGAL_ARGUMENT, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseResult handler(Exception e) {
        logger.warn("Handling exception: {}", e.getMessage());
        return ResponseResult.fail(e.getMessage());
    }

}
