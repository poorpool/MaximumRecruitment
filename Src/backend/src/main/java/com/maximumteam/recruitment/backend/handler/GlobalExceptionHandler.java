package com.maximumteam.recruitment.backend.handler;

import com.maximumteam.recruitment.backend.entity.ReturnMessage;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ReturnMessage jsonParamsException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder ret = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = String.format("%s", fieldError.getDefaultMessage());
            if (msg != null && !msg.isEmpty()) {
                return ReturnMessage.fail(400).setMessage(msg.toString());
            }
        }
        return ReturnMessage.fail(400).setMessage("未知错误");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ReturnMessage missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ReturnMessage.fail(400).setMessage(e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ReturnMessage authorizationException(AuthorizationException e) {
        return ReturnMessage.fail(400).setMessage(e.getMessage());
    }
}
