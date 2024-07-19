package com.yellobook.domains.post.entity;

import com.yellobook.domains.mentioned.entity.Mentioned;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ANNOUNCE")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announce extends Post {

    private String title;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<Mentioned> mentioned = new ArrayList<>();
}
