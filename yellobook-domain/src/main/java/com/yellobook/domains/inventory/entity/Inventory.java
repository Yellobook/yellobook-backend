package com.yellobook.domains.inventory.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.teamspace.entity.Teamspace;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "inventories")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Inventory extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamspace_id", nullable = false)
    private Teamspace teamspace;

    @Column(nullable = false)
    private String title;

    private Integer view;
}
