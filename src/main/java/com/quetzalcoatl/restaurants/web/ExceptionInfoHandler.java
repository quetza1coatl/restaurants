package com.quetzalcoatl.restaurants.web;

import com.quetzalcoatl.restaurants.util.ValidationUtil;
import com.quetzalcoatl.restaurants.util.exceptions.ErrorInfo;
import com.quetzalcoatl.restaurants.util.exceptions.LateToVoteException;
import com.quetzalcoatl.restaurants.util.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);
    private static Map<String, String> constraintCodeMap = new HashMap<>() {
        {
            put("users_unique_email_idx", "User with this email already exists");
            put("restaurants_address_idx", "Restaurant with this address already exists");
            put("dishes_name_idx", "Dish with this name already exists");

        }
    };


    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY) // 422
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        log.error("At request " + req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), e);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage().toLowerCase();
        if (rootMsg != null) {
            Optional<Map.Entry<String, String>> entry = constraintCodeMap.entrySet().stream()
                    .filter((it) -> rootMsg.contains(it.getKey()))
                    .findAny();
            if (entry.isPresent()) {
                e = new DataIntegrityViolationException(entry.get().getValue());
            }
        }

        log.error("At request " + req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), e);
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)  // 406
    @ExceptionHandler(LateToVoteException.class)
    public ErrorInfo lateToVote(HttpServletRequest req, LateToVoteException e) {
        log.error("At request " + req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), e);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo bindValidationError(HttpServletRequest req, Exception e) {
        BindingResult result = ((MethodArgumentNotValidException) e).getBindingResult();

        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(
                fe -> {
                    String msg = fe.getDefaultMessage();
                    if (msg != null) {
                        if (!msg.startsWith(fe.getField())) {
                            msg = fe.getField() + ' ' + msg;
                        }
                        joiner.add(msg);
                    }
                });

        return new ErrorInfo(req.getRequestURL(), e, joiner.toString());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        log.error("At request " + req.getRequestURL(), e);
        return new ErrorInfo(req.getRequestURL(), e);
    }
}

