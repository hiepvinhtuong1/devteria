package com.devteria.identity_service.repository;

import com.devteria.identity_service.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
