package it.trinex.spespappbe.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestore globale delle eccezioni per l'applicazione.
 * Gestisce tutte le eccezioni e restituisce risposte di errore appropriate.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce le eccezioni base personalizzate.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatus(),
                ex.getStatus().getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    /**
     * Gestisce le eccezioni di autenticazione di Spring Security.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("UNAUTHORIZED")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .message("Authentication required")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gestisce le eccezioni di credenziali errate di Spring Security.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("UNAUTHORIZED")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .message("Credenziali non valide")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gestisce le eccezioni di accesso negato di Spring Security.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("UNAUTHORIZED")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .message("Authentication required")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gestisce le eccezioni quando non viene trovato nessun handler per la richiesta (404).
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Non trovato",
                "Risorsa non trovata: " + request.getRequestURI(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Gestisce le eccezioni di deserializzazione JSON (es. valori enum non validi).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        String errorMessage = "Errore nella deserializzazione del JSON";
        
        if (ex.getCause() != null) {
            String causeMessage = ex.getCause().getMessage();
            if (causeMessage.contains("Cannot deserialize value of type")) {
                int fieldStart = causeMessage.indexOf("`");
                int fieldEnd = causeMessage.indexOf("`", fieldStart + 1);
                if (fieldStart > 0 && fieldEnd > fieldStart) {
                    String fieldType = causeMessage.substring(fieldStart + 1, fieldEnd);
                    String className = fieldType.substring(fieldType.lastIndexOf('.') + 1);
                    
                    int fromStart = causeMessage.indexOf("from String \"");
                    int fromEnd = causeMessage.indexOf("\"", fromStart + 14);
                    String invalidValue = (fromStart > 0 && fromEnd > fromStart) ? 
                            causeMessage.substring(fromStart + 14, fromEnd) : "valore non valido";
                    
                    errorMessage = className + ": '" + invalidValue + "' non è un valore valido";
                }
            }
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Errore di Validazione",
                errorMessage,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestisce le eccezioni di validazione dalle annotazioni @Valid sui BodyParam.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Errore di Validazione",
                errorMessage,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestisce le eccezioni di violazione dei vincoli sul singolo parametro (RequestParam, PathVariable).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Errore di Validazione",
                errorMessage,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestisce le eccezioni di binding.
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Errore di Validazione",
                errorMessage,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles illegal argument exceptions.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Token scaduto",
                ex.getMessage(),
                request.getRequestURI()
        );
        // aggiungi header X-Flag-Unauthorized: true alla risposta
        // serve lato frontend in interceptor per sloggare immediatamente la sessione
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Flag-Unauthorized", "true");
        return new ResponseEntity<>(errorResponse, headers, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gestisce le eccezioni di violazione dell'integrità dei dati (es. chiavi duplicate).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = "Violazione dell'integrità dei dati";

        // Tenta di estrarre un messaggio più specifico per le chiavi duplicate
        if (ex.getRootCause() != null) {
            String rootMsg = ex.getRootCause().getMessage();
            if (rootMsg.contains("Duplicate entry") || rootMsg.contains("duplicate key")) {
                message = "Chiave duplicata: un record con questo valore esiste già.";
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,
                "Conflitto",
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Resource not found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Fallback handler per tutte le altre eccezioni non gestite.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Internal Server Error",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
