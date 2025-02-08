package com.yellobook.api.controller.v1.terms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.api.controller.v1.terms.dto.request.TermsAgreementRequest;
import com.yellobook.api.controller.v1.terms.dto.response.TermsAgreementResponse;
import com.yellobook.core.domain.terms.TermsAgreement;
import com.yellobook.core.domain.terms.TermsService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TermsController.class)
class TermsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TermsService termsService;


    @Test
    void agreeToTerms_Success() throws Exception {
        long agreementId = 1L;
        long memberId = 1L;
        long termsId = 10L;

        LocalDateTime agreeAt = LocalDateTime.now();
        TermsAgreementRequest request = new TermsAgreementRequest(termsId, List.of(100L, 101L));
        TermsAgreement termsAgreement = new TermsAgreement(agreementId, memberId, termsId, agreeAt);
        given(termsService.agreeToActiveTerms(any(), any())).willReturn(termsAgreement);
        TermsAgreementResponse response = new TermsAgreementResponse(agreementId, memberId, termsId, agreeAt);

        given(termsService.agreeToActiveTerms(any(), any())).willReturn(termsAgreement);

        // When & Then
        mockMvc.perform(post("/api/v1/terms/agreements")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.agreed").value(true));
    }
}
