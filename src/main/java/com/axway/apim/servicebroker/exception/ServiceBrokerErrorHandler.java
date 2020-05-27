package com.axway.apim.servicebroker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@RestController
public class ServiceBrokerErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceBrokerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleException(ServiceBrokerException ex) {
        return getErrorResponse(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(AxwayException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleAxwayException(AxwayException ex) {
        return new ErrorMessage(ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ErrorMessage handleAllExceptions(Exception ex) {
        return new ErrorMessage(ex.getMessage());
    }



    protected ErrorMessage getErrorResponse(ServiceBrokerException ex) {
       return ex.getErrorMessage();
    }
}
