package com.strix.msusuarios.repository;

import com.strix.msusuarios.model.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    boolean existsByTokenHash(String tokenHash);
}
