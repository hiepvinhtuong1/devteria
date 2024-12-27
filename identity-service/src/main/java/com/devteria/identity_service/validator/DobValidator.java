package com.devteria.identity_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Validator để kiểm tra ngày sinh (dob - date of birth) có đáp ứng yêu cầu về tuổi tối thiểu hay không.
 */
public class DobValidator implements ConstraintValidator<DobContraint, LocalDate> {

    private int min; // Giá trị tuổi tối thiểu được cấu hình từ annotation @DobConstraint

    /**
     * Phương thức khởi tạo validator, lấy thoong tin cấu hình từ annotation
     *
     * @param constraintAnnotation: annotation chứa thông tin cấu hình(ví dụ: giá trị min)
     */
    @Override
    public void initialize(DobContraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min(); // gán giá trị min từ annotation
    }

    /**
     * Phương thức kiểm tra giá trị ngày sinh có hợp lệ hay không
     *
     * @param value Giá trị ngày sinh cần kiểm tra
     * @param constraintValidatorContext Bối cảnh của quá trình xác thực( dùng để cấu hình thông bãos lỗi nếu cần)
     * @return: true nếu giá trị hợp lệ, false neeus không
     */
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(value))
            // trả về true nếu giá trị là null, tránh lỗi xác thực không cần thiết
            return true;

        // tỉnh khoảng cách(theo năm) giữa ngày sinh và hiện tại
        long years = ChronoUnit.YEARS.between(value, LocalDate.now());

        // Kiểm tra số năm nếu lớn hơn hoặc bằng số tuổi thì hợp lệ
        return years >= min;
    }


}
