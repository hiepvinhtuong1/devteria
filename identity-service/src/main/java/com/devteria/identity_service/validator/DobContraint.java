package com.devteria.identity_service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// Định nghĩa annotation tùy chỉnh để ràng buộc (constraint) việc kiểm tra ngày sinh (dob - date of birth).
@java.lang.annotation.Target({ java.lang.annotation.ElementType.FIELD }) // Áp dụng cho các trường (field) của class.
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME) // Annotation tồn tại ở thời điểm runtime để sử dụng cho xác thực.
@jakarta.validation.Constraint(validatedBy = { DobValidator.class }) // Xác định validator được sử dụng là DobValidator.
// @inteface để thông báo day la mot anotation
public @interface DobContraint {

    /**
     * Thông báo lỗi mặc định nếu giá trị không hợp lệ.
     *
     * @return Thông báo lỗi.
     */
    String message() default "Invalid date of birth";

    int min();

    /**
     * Nhóm xác thực (groups), dùng để phân loại các ràng buộc. Mặc định không sử dụng.
     *
     * @return Nhóm xác thực.
     */
    Class<?>[] groups() default {};

    /**
     * Metadata bổ sung cho ràng buộc, được sử dụng trong các ngữ cảnh đặc biệt.
     *
     * @return Mảng các payload.
     */
    Class<? extends Payload>[] payload() default {};
}
