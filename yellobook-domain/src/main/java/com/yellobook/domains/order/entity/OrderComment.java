package com.yellobook.domains.order.entity;

import com.yellobook.domains.common.entity.Comment;
import com.yellobook.enums.CommentType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(CommentType.Values.ORDER_COMMENT)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderComment extends Comment {
}
