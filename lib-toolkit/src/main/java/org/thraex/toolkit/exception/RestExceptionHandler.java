package org.thraex.toolkit.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.response.ResponseStatus;

/**
 * TODO: Opt Exception
 *
 * @author 鬼王
 * @date 2022/03/10 11:13
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

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
        logger.error("Handling exception", e);
        return ResponseResult.fail(e.getMessage());
    }

}
