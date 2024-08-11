package com.yellobook.domains.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.schedule.ScheduleService;
import com.yellobook.domains.schedule.dto.request.DailyParam;
import com.yellobook.domains.schedule.dto.request.MonthlyParam;
import com.yellobook.domains.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domains.schedule.dto.response.CalendarResponse;
import com.yellobook.domains.schedule.dto.response.DailyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.SearchMonthlyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.UpcomingScheduleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.beans.PropertyDescriptor;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
class ScheduleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final TeamMemberVO teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);

    @MockBean
    private ScheduleService scheduleService;


    @MockBean
    private TeamMemberArgumentResolver teamMemberArgumentResolver;

    @BeforeEach
    void setUp() throws Exception {
        when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(teamMemberVO);
    }

    @Test
    @DisplayName("다가오는 일정")
    void upcomingSchedulesTest() throws Exception {
        // given
        var response = UpcomingScheduleResponse.builder()
                .scheduleTitle("일정 제목")
                .build();
        when(scheduleService.getUpcomingSchedules(teamMemberVO)).thenReturn(response);

        // when & then
        mockMvc.perform(get(reqURL("/upcoming")))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        verify(scheduleService, times(1)).getUpcomingSchedules(teamMemberVO);
    }

    @Test
    @DisplayName("월별 키워드에 해당하는 일정")
    void searchMonthlySchedulesTest() throws Exception {
        // given
        var request = MonthlySearchParam.builder()
                .year(2024)
                .month(8)
                .keyword("일정")
                .build();
        var response = SearchMonthlyScheduleResponse.builder()
                .schedules(List.of())
                .build();

        when(scheduleService.searchMonthlySchedules(request, teamMemberVO)).thenReturn(response);
        var reqParam = convertToMultiValueMap(request);

        // when & then
        mockMvc.perform(get(reqURL("/search"))
                        .params(reqParam)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        verify(scheduleService, times(1)).searchMonthlySchedules(request, teamMemberVO);
    }

    @Test
    @DisplayName("캘린더 월별 종합 일정")
    void getCalendarSchedulesTest() throws Exception {
        // given
        var request = MonthlyParam.builder()
                .year(2024)
                .month(8)
                .build();
        var response = CalendarResponse.builder()
                .calendar(List.of())
                .build();
        var reqParams = convertToMultiValueMap(request);

        // when & then
        mockMvc.perform(get(reqURL("/monthly"))
                        .params(reqParams)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        verify(scheduleService, times(1)).getCalendarSchedules(request, teamMemberVO);
    }

    @Test
    @DisplayName("날짜별 일정")
    void getDailySchedulesTest() throws Exception {
        // given
        var request = DailyParam.builder()
                .year(2024)
                .month(8)
                .day(11)
                .build();
        var response = DailyScheduleResponse.builder()
                .schedules(List.of())
                .build();
        var reqParams = convertToMultiValueMap(request);

        // when & then
        mockMvc.perform(get(reqURL("/daily"))
                        .params(reqParams)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        verify(scheduleService, times(1)).getDailySchedules(request, teamMemberVO);
    }


    private String reqURL(String value) {
        String URL_PREFIX = "/api/v1/schedule";
        return URL_PREFIX + value;
    }


    private MultiValueMap<String, String> convertToMultiValueMap(Object obj) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);

        for (PropertyDescriptor descriptor : beanWrapper.getPropertyDescriptors()) {
            String fieldName = descriptor.getName();
            Object fieldValue = beanWrapper.getPropertyValue(fieldName);

            if (fieldValue != null && !fieldName.equals("class")) {
                paramMap.add(fieldName, fieldValue.toString());
            }
        }
        return paramMap;
    }

}