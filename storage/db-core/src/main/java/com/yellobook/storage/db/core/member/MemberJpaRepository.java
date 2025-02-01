package com.yellobook.storage.db.core.member;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByOauthIdAndOauthProvider(String oauthId, String oauthProvider);

    @Query("SELECT m.nicknameUpdatedAt FROM MemberEntity m WHERE m.id = :id")
    LocalDateTime findNicknameUpdatedAt(@Param("id") Long memberId);
}