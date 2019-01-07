package com.quetzalcoatl.restaurants.web;

import com.quetzalcoatl.restaurants.util.exceptions.ErrorInfo;
import com.quetzalcoatl.restaurants.util.exceptions.LateToVoteException;
import com.quetzalcoatl.restaurants.util.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        log.error("At request " + req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), e);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        log.error("At request " + req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), e);
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)  // 406
    @ExceptionHandler(LateToVoteException.class)
    public ErrorInfo lateToVote(HttpServletRequest req, LateToVoteException e) {
        log.error("At request " + req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), e);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        log.error("At request " + req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), e);
    }
}

