package com.devteria.identity_service.dto.request;

import com.devteria.identity_service.validator.DobContraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
     String password;
     String firstName;
     String lastName;

     @DobContraint(min = 18, message = "INVALID_DOB")
     LocalDate dob;

     List<String> roles;
}
