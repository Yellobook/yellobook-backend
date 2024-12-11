package com.yellobook.domains.inform.repository;

import com.yellobook.domains.inform.entity.Inform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformRepository extends JpaRepository<Inform, Long> {
}
