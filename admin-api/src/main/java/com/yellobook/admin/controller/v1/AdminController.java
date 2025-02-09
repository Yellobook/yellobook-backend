package com.yellobook.admin.controller.v1;

import com.yellobook.admin.controller.v1.dto.request.CreateTermsRequest;
import com.yellobook.admin.controller.v1.dto.response.CreateTermsResponse;
import com.yellobook.admin.domain.AdminTermsService;
import com.yellobook.admin.support.response.AdminResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/v1")
public class AdminController implements AdminApiDocs {
    private final AdminTermsService adminTermsService;

    public AdminController(AdminTermsService adminTermsService) {
        this.adminTermsService = adminTermsService;
    }

    @PostMapping("/terms")
    public AdminResponse<CreateTermsResponse> createTerms(
            @RequestBody CreateTermsRequest request
    ) {
        var result = adminTermsService.createNewTerms(request.toNewTerms());
        return AdminResponse.success(CreateTermsResponse.of(result));
    }
}