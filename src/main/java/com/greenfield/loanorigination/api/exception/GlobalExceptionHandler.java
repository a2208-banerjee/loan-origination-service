package com.greenfield.loanorigination.api.exception;

import com.greenfield.loanorigination.common.exception.CreditBureauUnavailableException;
import com.greenfield.loanorigination.common.exception.DuplicateApplicationException;
import com.greenfield.loanorigination.common.exception.InvalidStateTransitionException;
import com.greenfield.loanorigination.common.exception.LoanNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ApiError> handleLoanNotFoundException(LoanNotFoundException ex, HttpServletRequest request) {
        return buildApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<ApiError> handleDuplicate(DuplicateApplicationException ex, HttpServletRequest req) {
        // DataIntegrityViolationException catches the race the app-level idempotency check
        // can miss — the unique constraint on idempotency_key is the actual guarantee
        return buildApiError(HttpStatus.CONFLICT, "Duplicate submission", req);
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<ApiError> handleInvalidTransition(InvalidStateTransitionException ex, HttpServletRequest req) {
        return buildApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), req);
    }

    @ExceptionHandler(CreditBureauUnavailableException.class)
    public ResponseEntity<ApiError> handleBureauUnavailable(CreditBureauUnavailableException ex, HttpServletRequest req) {
        return buildApiError(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), req);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest req) {
        return buildApiError(HttpStatus.CONFLICT, "Duplicate Request Submission", req);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
//        String message = ex.getBindingResult().getFieldErrors().stream()
//                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
//                .reduce((fe1, fe2) -> fe1 + ":" + fe2)
//                .orElse("Validation failed");
//        return buildApiError(HttpStatus.BAD_REQUEST, message, request);
//    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .reduce((fe1, fe2) -> fe1 + ":" + fe2)
                .orElse("Validation failed");

        String path = request instanceof ServletWebRequest servletWebRequest
                ? servletWebRequest.getRequest().getRequestURI()
                : request.getDescription(false);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), message, path));
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest req) {
//        return buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req);
//    }

    @ExceptionHandler(Exception.class)
    //Use ExceptionHandler annotation to handle specific annotation
    public ResponseEntity<Object> handleGlobalException(Exception ex,
                                                        HttpHeaders headers,
                                                        HttpStatusCode status,
                                                        WebRequest request){
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";

        String path = request instanceof ServletWebRequest servletWebRequest
                ? servletWebRequest.getRequest().getRequestURI()
                : request.getDescription(false);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), message, path));

    }

    public ResponseEntity<ApiError> buildApiError(HttpStatus status, String errorMessage, HttpServletRequest request){
        return ResponseEntity.status(status).body(ApiError.of(status.value(), status.getReasonPhrase(), errorMessage, request.getRequestURI()));
    }




}
