package startup.spring_auth.application.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import startup.spring_auth.application.payload.ApiResponse;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalException {
    Logger logger = LoggerFactory.getLogger(GlobalException.class);

    //      Barcha Exception turlari uchun umumiy xatolik tutuvchi metod
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        ApiResponse<String> errorResponse = ApiResponse.error("INTERNAL_SERVER_ERROR", 500);

        logger.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    //     Barcha RuntimeException turlari uchun umumiy xatolik tutuvchi metod
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<String> errorResponse = ApiResponse.error("INTERNAL_SERVER_ERROR", 500);

        logger.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    //     MethodArgumentNotValidException xatoligini tutuvchi metod
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiResponse<String> errorResponse = ApiResponse
                .error(Objects.requireNonNull(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()), 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    //     BadRequestException  xatoligini tutuvchi metod
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<String>> handleBadRequestException(BadRequestException ex) {
        ApiResponse<String> errorResponse = ApiResponse.error(ex.getMessage(), 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    //     AuthorizationDeniedException  xatoligini tutuvchi metod
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ApiResponse<String> errorResponse = ApiResponse.error(ex.getMessage(), 403);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    //     ForbiddenException xatoligini tutuvchi metod
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<String>> handleForbiddenException(ForbiddenException ex) {
        ApiResponse<String> errorResponse = ApiResponse.error(ex.getMessage(), 403);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    //     ConflictException xatoligini tutuvchi metod
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<String>> handleConflictException(ConflictException ex) {
        ApiResponse<String> errorResponse = ApiResponse.error(ex.getMessage(), 409);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    //     NotFoundException xatoligini tutuvchi metod
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(NotFoundException ex) {
        ApiResponse<String> errorResponse = ApiResponse.error(ex.getMessage(), 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
