package de.eon.cidgen.controller;

import de.eon.cidgen.dto.ApiError;
import de.eon.cidgen.service.CidRangeExhaustedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Converts expected failures into small JSON responses instead of stack traces.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationError(MethodArgumentNotValidException exception) {
        String message = "Request is invalid";
        if (exception.getBindingResult().getFieldError() != null) {
            message = exception.getBindingResult().getFieldError().getDefaultMessage();
        }
        return new ApiError(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidArgument(IllegalArgumentException exception) {
        return new ApiError(exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataConflict(DataIntegrityViolationException exception) {
        return new ApiError("The identifier could not be created uniquely");
    }

    @ExceptionHandler(CidRangeExhaustedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRangeExhausted(CidRangeExhaustedException exception) {
        return new ApiError(exception.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEndpointNotFound(NoResourceFoundException exception) {
        return new ApiError("Endpoint not found");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoHandlerFound(NoHandlerFoundException exception) {
        return new ApiError("Endpoint not found");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnexpectedError(Exception exception) {
        return new ApiError("An unexpected server error occurred");
    }
}
