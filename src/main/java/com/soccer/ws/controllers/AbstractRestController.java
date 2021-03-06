package com.soccer.ws.controllers;

import com.soccer.ws.dto.ErrorDetailDTO;
import com.soccer.ws.dto.LocalizedMessageDTO;
import com.soccer.ws.dto.ValidationErrorDTO;
import com.soccer.ws.dto.ValidationErrorDetailDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.exceptions.InvalidRecoveryCodeException;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.exceptions.UnauthorizedAccessException;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.SanitizeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User: Tom De Dobbeleer
 * Date: 1/20/14
 * Time: 1:43 PM
 * Remarks: none
 */
@RequestMapping("/api/v1")
public abstract class AbstractRestController extends AbstractSecurityController {
    private static final Logger log = LoggerFactory.getLogger(AbstractRestController.class);
    final
    MessageSource messageSource;
    private final Function<FieldError, ValidationErrorDTO> fieldErrorToValidationError = fieldError -> {
        ValidationErrorDTO v = new ValidationErrorDTO();
        v.setField(fieldError.getField());
        v.setCode(fieldError.getCode());
        LocalizedMessageDTO localizedMessage = new LocalizedMessageDTO();
        localizedMessage.setMessageEn(getMessage(fieldError.getCode(), Locale.ENGLISH));
        localizedMessage.setMessageNl(getMessage(fieldError.getCode(), new Locale("nl")));
        v.setLocalizedMessageDTO(localizedMessage);
        log.debug("fieldErrorToValidationError - Transforming fieldError into validationError succesful, returing object", SanitizeUtils.sanitizeHtml(v.toString()));
        return v;
    };

    public AbstractRestController(SecurityUtils securityUtils, MessageSource messageSource) {
        super(securityUtils);
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        ValidationErrorDetailDTO errorDetail = createErrorDetail(new ValidationErrorDetailDTO(getValidationErrors(e)),
                HttpStatus.BAD_REQUEST.value(),
                request,
                e);
        log.warn("handleValidationError - Validation errors found ({}), returning http status {}", errorDetail.getValidationErrorDTOList(), HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRecoveryCodeException.class)
    public ResponseEntity<?> handleInvalidRecoveryCode(InvalidRecoveryCodeException e, HttpServletRequest request) {
        ErrorDetailDTO errorDetail = createErrorDetail(new ErrorDetailDTO(),
                HttpStatus.BAD_REQUEST.value(),
                request,
                e);
        log.warn("handleInvalidRecoveryCode - Bad recovery code, returning http status {}", HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccesDenied(AccessDeniedException e, HttpServletRequest request) {
        ErrorDetailDTO errorDetail = createErrorDetail(new ErrorDetailDTO(),
                HttpStatus.FORBIDDEN.value(),
                request,
                e);
        log.warn("handleInternalError - Internal error: {}, returning http status {}", e.getMessage(), HttpStatus.FORBIDDEN.name());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<?> handleUnAuth(UnauthorizedAccessException e, HttpServletRequest request) {
        ErrorDetailDTO errorDetail = createErrorDetail(new ErrorDetailDTO(),
                HttpStatus.UNAUTHORIZED.value(),
                request,
                e);
        log.warn("handleUnAuth - Unauthorized error: {}, returning http status {}", e.getMessage(), HttpStatus.UNAUTHORIZED.name());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        ErrorDetailDTO errorDetail = createErrorDetail(new ErrorDetailDTO(),
                HttpStatus.UNAUTHORIZED.value(),
                request,
                e);
        log.warn("handleBadCredentials - Bad credentials error: {}, returning http status {}", e.getMessage(), HttpStatus
                .UNAUTHORIZED.name());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handleObjNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        ErrorDetailDTO errorDetail = createErrorDetail(new ErrorDetailDTO(),
                HttpStatus.NOT_FOUND.value(),
                request,
                e);
        log.warn("handleObjNotFound - Object not found error: {}, returning http status {}", e.getMessage(), HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalError(Exception e, HttpServletRequest request) {
        ErrorDetailDTO errorDetail = createErrorDetail(new ErrorDetailDTO(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request,
                e);
        log.warn("handleInternalError - Internal error: {}, returning http status {}", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected void validate(BindingResult result) throws CustomMethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomMethodArgumentNotValidException(result);
        }
    }

    protected void validate(final Validator validator, final Object target, final BindingResult result) throws CustomMethodArgumentNotValidException {
        //Validate first
        validator.validate(target, result);
        //Then check result
        validate(result);
    }

    private <T extends ErrorDetailDTO> T createErrorDetail(T errorDetail, int status, HttpServletRequest request, Exception e) {
        errorDetail.setStatus(status);
        errorDetail.setPath(getRequestURI(request));
        errorDetail.setDetail(e.getMessage());
        errorDetail.setDeveloperMessage(e.getClass().getName());
        return errorDetail;
    }

    private String getRequestURI(HttpServletRequest request) {
        return (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
    }

    private List<ValidationErrorDTO> getValidationErrors(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return fieldErrors
                .stream()
                .map(fieldErrorToValidationError)
                .collect(Collectors.toList());
    }

    private String getMessage(final String messageCode, final Locale locale) {
        try {
            return messageSource.getMessage(messageCode, new Object[]{}, locale);
        } catch (Exception e) {
            return messageCode;
        }
    }
}
