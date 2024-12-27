package com.devteria.identity_service.exception;

import com.devteria.identity_service.dto.request.ApiResponse;
import jakarta.validation.ConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import jakarta.validation.ConstraintViolation;

import java.util.Map;
import java.util.Objects;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Hằng số định nghĩa cho key "min" trong attribute của constraint
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(e.getErrorCode().getCode());
        apiResponse.setMessage(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatus()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse();

        System.out.println(exception);
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Xử lý ngoại lệ khi tham số đầu vào không hợp lệ (MethodArgumentNotValidException)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException( MethodArgumentNotValidException e) {
        // Lấy thông báo lỗi mặc định từ FieldError
        String enumKey = e.getFieldError().getDefaultMessage();

        // Gán mã lỗi mặc định là ErrorCode.INVALID_KEY
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        try {

            // Kiểm tra thông điệp tương ứng với một mã lỗi trong errorCode
            errorCode = ErrorCode.valueOf(enumKey);

            // lấy thông tin chi tiết từ ConstraintViolation
            var costraintViolation = e.getBindingResult()
                    .getAllErrors()
                        .get(0)
                    .unwrap(ConstraintViolation.class); //được sử dụng để chuyển đổi một lỗi (error) được bọc trong đối tượng ObjectError hoặc FieldError sang kiểu ConstraintViolation.

            log.info("BindResult: "+e.getBindingResult().toString());

            // Trích xuất các attributes của constraint (nếu có)
            attributes = costraintViolation.getConstraintDescriptor().getAttributes();
        } catch (IllegalArgumentException e1 ){
            // Nếu không tìm thấy mã lỗi tương ứng, sử dụng ErrorCode.INVALID_KEY
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage()
        );

        return ResponseEntity.status(errorCode.getStatus()).body(apiResponse);
    }

    // Phương thức thay thế các giá trị tham số trong thông điệp lỗi
    private String mapAttribute(String message, Map<String, Object> attributes) {
        // Lấy giá trị "min" từ attributes và thay thế vào thông điệp
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
