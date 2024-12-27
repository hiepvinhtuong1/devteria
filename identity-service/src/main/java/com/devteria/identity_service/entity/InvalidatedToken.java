package com.devteria.identity_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
// Entity chứa các token đã bị logout
public class InvalidatedToken {
    @Id
    String id;
    Date expiryTime;
}
