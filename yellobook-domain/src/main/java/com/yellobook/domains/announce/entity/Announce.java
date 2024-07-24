package com.yellobook.domains.announce.entity;

import com.yellobook.domains.common.entity.Post;
import com.yellobook.enums.PostType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(PostType.Values.ANNOUNCE)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announce extends Post {
    private String title;
}
