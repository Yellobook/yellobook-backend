package com.yellobook.domain.announce.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/announce")
@Tag(name = "\uD83D\uDCE2 공지", description = "Announce API")
public class AnnounceController {
}
