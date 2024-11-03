package com.yellobook.domains.schedule.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.common.enums.ScheduleType;
import com.yellobook.domains.schedule.dto.query.QuerySchedule;
import com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule;
import com.yellobook.domains.schedule.dto.response.CalendarResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("ScheduleMapper Unit Test")
class ScheduleMapperTest {
    private final ScheduleMapper mapper = Mappers.getMapper(ScheduleMapper.class);

    @Nested
    @DisplayName("toCalendarResponse 메서드는")
    class Describe_toCalendarResponse {
        @Nested
        @DisplayName("Map<Integer, List<String>> 를 받아")
        class Context_with_calendarMap_is_mapped {
            Map<Integer, List<String>> calendarMap;

            @BeforeEach
            void setUpContext() {
                calendarMap = new HashMap<>();
                calendarMap.put(8, List.of("주문1", "주문2"));
                calendarMap.put(31, List.of("공지1", "공지2", "공지3"));
            }

            @Test
            @DisplayName("CalendarResponse로 반환한다.")
            void it_returns_CalendarResponse() {
                CalendarResponse target = mapper.toCalendarResponse(calendarMap);
                assertThat(target).isNotNull();
                assertThat(target.calendar()).hasSize(calendarMap.size());

                IntStream.range(0, target.calendar()
                                .size())
                        .forEach(i -> {
                            var targetCalendarItem = target.calendar()
                                    .get(i);
                            var sourceTitles = calendarMap.get(targetCalendarItem.day());
                            assertThat(targetCalendarItem.titles()).containsExactlyElementsOf(sourceTitles);
                        });
            }
        }
    }

    @Nested
    @DisplayName("toUpcomingScheduleResponse 메서드는")
    class Describe_upcomingScheduleResponse {
        @Nested
        @DisplayName("QueryUpcomingSchedule 을 받아")
        class Context_with_queryUpcomingSchedule_is_mapped {
            QueryUpcomingSchedule queryUpcomingSchedule;

            @BeforeEach
            void setUpContext() {
                queryUpcomingSchedule = QueryUpcomingSchedule.builder()
                        .title("공지1")
                        .build();
            }

            @Test
            @DisplayName("UpcomingScheduleResponse를 반환한다.")
            void it_returns_UpcomingScheduleResponse() {
                var target = mapper.toUpcomingScheduleResponse(queryUpcomingSchedule);
                assertThat(target.scheduleTitle()).isEqualTo(queryUpcomingSchedule.title());
            }
        }

        @Nested
        @DisplayName("String 타입 값을 받아")
        class Context_with_string_is_mapped {
            String title;

            @BeforeEach
            void setUpContext() {
                title = "주문1";
            }

            @Test
            @DisplayName("UpcomingScheduleResponse를 반환한다.")
            void it_returns_UpcomingScheduleResponse() {
                var target = mapper.toUpcomingScheduleResponse(title);
                assertThat(target).isNotNull();
                assertThat(target.scheduleTitle()).isEqualTo(title);
            }
        }
    }


    @Nested
    @DisplayName("toSearchMonthlyScheduleResponse 메서드는")
    class Describe_searchMonthlyScheduleResponse {
        @Nested
        @DisplayName("List<QuerySchedule>을 받아")
        class Context_with_schedules_is_mapped {
            List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = List.of(
                        QuerySchedule.builder()
                                .id(1L)
                                .title("공지2")
                                .scheduleType(ScheduleType.INFORM)
                                .date(LocalDate.of(2024, 11, 3))
                                .build(),
                        QuerySchedule.builder()
                                .id(1L)
                                .title("주문1")
                                .scheduleType(ScheduleType.ORDER)
                                .date(LocalDate.of(2024, 11, 3))
                                .build()
                );
            }

            @Test
            @DisplayName("SearchMonthlyScheduleResponse를 반환한다.")
            void it_returns_SearchMonthlyScheduleResponse() {
                var target = mapper.toSearchMonthlyScheduleResponse(schedules);
                assertThat(target.schedules()).isNotNull();
                assertThat(target.schedules()).hasSize(2);
                assertThat(target.schedules()).containsExactlyElementsOf(schedules);
            }
        }
    }

    @Nested
    @DisplayName("toDailyScheduleResponse 메서드는")
    class Describe_toDailyScheduleResponse {
        @Nested
        @DisplayName("List<QuerySchedule> 을 받아")
        class Context_with_schedules_is_mapped {
            List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = List.of(
                        QuerySchedule.builder()
                                .id(1L)
                                .title("공지2")
                                .scheduleType(ScheduleType.INFORM)
                                .date(LocalDate.of(2024, 11, 3))
                                .build(),
                        QuerySchedule.builder()
                                .id(1L)
                                .title("주문1")
                                .scheduleType(ScheduleType.ORDER)
                                .date(LocalDate.of(2024, 11, 3))
                                .build()
                );
            }

            @Test
            @DisplayName("DailyScheduleResponse 를 반환한다.")
            void it_returns_DailyScheduleResponse() {
                var target = mapper.toDailyScheduleResponse(schedules);
                assertThat(target.schedules()).isNotNull();
                assertThat(target.schedules()).hasSize(2);
                assertThat(target.schedules()).containsExactlyElementsOf(schedules);
            }
        }
    }
}