package com.yellobook.domain.inform.service;


import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;

public interface InformQueryService {
    Inform getInformById(Long informId);
    InformComment getCommentByInformId(Long informId);
}
