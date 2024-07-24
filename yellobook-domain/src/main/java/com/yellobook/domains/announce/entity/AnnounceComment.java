package com.yellobook.domains.announce.entity;

import com.yellobook.domains.common.entity.Comment;
import com.yellobook.enums.CommentType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(CommentType.Values.ANNOUNCE_COMMENT)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnounceComment extends Comment {
}
