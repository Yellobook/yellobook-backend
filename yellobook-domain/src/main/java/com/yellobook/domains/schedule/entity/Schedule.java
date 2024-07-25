package com.yellobook.domains.schedule.entity;

import com.yellobook.enums.PostType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Immutable
@Table(name = "v_schedule")
public class Schedule {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "team_id")
    private Long teamId;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
