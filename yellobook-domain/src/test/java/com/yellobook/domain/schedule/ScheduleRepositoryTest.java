package com.yellobook.domain.schedule;

import com.yellobook.config.TestConfig;
import com.yellobook.domains.schedule.dto.MonthlyScheduleDTO;
import com.yellobook.domains.schedule.dto.ScheduleDTO;
import com.yellobook.domains.schedule.dto.UpcomingScheduleDTO;
import com.yellobook.domains.schedule.repository.ScheduleRepository;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.ScheduleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = "classpath:setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // 모든 테스트 전에 한 번 실행
@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // 모든 테스트 후에 한 번 실행
@DisplayName("PostRepository 테스트")
public class ScheduleRepositoryTest {
    @Autowired
    private ScheduleRepository scheduleRepository;

    // today 를 고정해야 sql 파일 데이터를 반영할 수 있다.
    // date.after(today) 를 사용하기 때문에 now() 로 하면 시간에 따라 결과가 바뀐다.
    private final LocalDate today = LocalDate.of(2024, 7, 25);

    @Nested
    @DisplayName("findEarliestOrder 는")
    class FindEarliestOrderTests {

        @Test
        @DisplayName("주문자가 자신의 팀에 속한 자신이 주문한 가장 이른 주문을 가져온다")
        void testFindEarliestOrderForOrderer() {
            // given
            Long teamId = 1L;
            Long ordererId = 2L;

            // when
            Optional<UpcomingScheduleDTO> result = scheduleRepository.findEarliestOrder(today, MemberTeamRole.ORDERER, teamId, ordererId);

            // then
            assertThat(result).isPresent();
            UpcomingScheduleDTO schedule = result.get();
            assertThat(schedule.getTitle()).isEqualTo("제품 A 서브제품1 5개");
            assertThat(schedule.getDay()).isEqualTo("2024-07-27");
            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.ORDER.getValue());
        }

        @Test
        @DisplayName("관리자가 자신의 팀에 속한 가장 이른 주문을 가져온다")
        void testFindEarliestOrderForAdmin() {
            // given
            Long teamId = 1L;
            Long adminId = 1L;

            // when
            Optional<UpcomingScheduleDTO> result = scheduleRepository.findEarliestOrder(today, MemberTeamRole.ADMIN, teamId, adminId);

            // then
            assertThat(result).isPresent();
            UpcomingScheduleDTO schedule = result.get();
            assertThat(schedule.getTitle()).isEqualTo("제품 A 서브제품1 10개");
            assertThat(schedule.getDay()).isEqualTo("2024-07-26");
            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.ORDER.getValue());
        }

        @Test
        @DisplayName("팀이 존재하지 않는다면 결과가 비어 있어야 한다")
        void testFindEarliestOrderWithInvalidTeamId() {
            // given
            Long invalidTeamId = 999L;
            Long memberId = 1L;

            // when
            Optional<UpcomingScheduleDTO> result = scheduleRepository.findEarliestOrder(today, MemberTeamRole.ORDERER, invalidTeamId, memberId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("사용자가 존재하지 않는다면 결과가 비어 있어야 한다")
        void testFindEarliestOrderWithInvalidMemberId() {
            // given
            Long teamId = 1L;
            Long invalidMemberId = 999L;

            // when
            Optional<UpcomingScheduleDTO> result = scheduleRepository.findEarliestOrder(today, MemberTeamRole.ORDERER, teamId, invalidMemberId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findEarliestInform 는")
    class findEarliestInformTests {


        @Test
        @DisplayName("자신에게 관련된 가장 이른 공지 및 일정 을 가져온다")
        void testFindEarliestInformForMember() {
            // given
            Long teamId = 1L;
            Long memberId = 1L;

            // when
            Optional<UpcomingScheduleDTO> result = scheduleRepository.findEarliestInform(today, teamId, memberId);

            // then
            assertThat(result).isPresent();
            UpcomingScheduleDTO schedule = result.get();
            assertThat(schedule.getTitle()).isEqualTo("공지 및 일정1");
            assertThat(schedule.getDay()).isEqualTo(LocalDate.of(2024, 7, 26));
            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.INFORM.getValue());
        }

        @Test
        @DisplayName("팀 구성원이 아닌 경우 결과가 없어야 한다")
        void testFindEarliestInformForNonTeamMember() {
            // given
            Long teamId = 1L;
            Long memberId = 999L;

            // when
            Optional<UpcomingScheduleDTO> result = scheduleRepository.findEarliestInform(today, teamId, memberId);

            // then
            assertThat(result).isNotPresent();
        }

        @Test
        @DisplayName("태그된 일정이 존재하면 해당 일정도 가져온다")
        void testFindEarliestInformForMentionedMember() {
            // given
            Long teamId = 1L;
            Long memberId = 2L;

            // when
            Optional<UpcomingScheduleDTO> result = scheduleRepository.findEarliestInform(today, teamId, memberId);

            // then
            assertThat(result).isPresent();
            UpcomingScheduleDTO schedule = result.get();
            assertThat(schedule.getTitle()).isEqualTo("공지 및 일정1");
            assertThat(schedule.getDay()).isEqualTo(LocalDate.of(2024, 7, 26));
            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.INFORM.getValue());
        }
    }

    @Nested
    @DisplayName("searchMonthlyOrders 는")
    class searchMonthlyOrdersTests {

        @Test
        @DisplayName("특정 키워드를 포함한 특정 월의 주문 목록을 가져와야 한다.")
        void testSearchMonthlyOrdersForMember() {
            // given
            String keyword = "제품";
            MemberTeamRole role = MemberTeamRole.ORDERER;
            Long teamId = 1L;
            Long memberId = 1L;
            int year = 2024;
            int month = 7;

            // when
            List<ScheduleDTO> result = scheduleRepository.searchMonthlyOrders(keyword, role, teamId, memberId, year, month);

            // then
            assertThat(result.size()).isGreaterThan(0);
            assertThat(result).isNotEmpty();
            assertThat(result).extracting("title").contains("제품 A 서브제품1 10개", "제품 B 서브제품2 20개", "제품 A 서브제품1 18개");
            assertThat(result).extracting("scheduleType").containsOnly(ScheduleType.ORDER.getValue());
            assertThat(result).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
        }

        @Test
        @DisplayName("존재하지 않는 키워드로 검색 시 빈 리스트를 반환해야 한다.")
        void testSearchMonthlyOrdersWithNonExistentKeyword() {
            // given
            String keyword = "없는 키워드";
            MemberTeamRole role = MemberTeamRole.ORDERER;
            Long teamId = 1L;
            Long memberId = 1L;
            int year = 2024;
            int month = 7;

            // when
            List<ScheduleDTO> result = scheduleRepository.searchMonthlyOrders(keyword, role, teamId, memberId, year, month);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("특정 키워드와 연관된 주문 목록을 올바르게 정렬해야 한다.")
        void testSearchMonthlyOrdersSortedOrder() {
            // given
            String keyword = "제품";
            MemberTeamRole role = MemberTeamRole.ORDERER;
            Long teamId = 1L;
            Long memberId = 1L;
            int year = 2024;
            int month = 7;

            // when
            List<ScheduleDTO> result = scheduleRepository.searchMonthlyOrders(keyword, role, teamId, memberId, year, month);

            // then
            assertThat(result).isNotEmpty();
            assertThat(result).isSortedAccordingTo(Comparator.comparing(ScheduleDTO::getDate));
        }
    }

    @Nested
    @DisplayName("searchMonthlyInforms는")
    class SearchMonthlyInformsTests {

        @Test
        @DisplayName("자신이 작성한 일정을 검색할 수 있다")
        void testSearchMonthlyInformsForOwn() {
            // given
            String keyword = "일정";
            Long teamId = 1L;
            Long memberId = 1L;
            int year = 2024;
            int month = 7;

            // when
            List<ScheduleDTO> schedules = scheduleRepository.searchMonthlyInforms(keyword, MemberTeamRole.ADMIN, teamId, memberId, year, month);

            // then
            assertThat(schedules).isNotEmpty();
            assertThat(schedules).extracting("title").contains("공지 및 일정7");
            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
        }

        @Test
        @DisplayName("자신이 태그된 일정을 검색할 수 있다")
        void testSearchMonthlyInformsForTagged() {
            // given
            String keyword = "일정";
            Long teamId = 1L;
            Long memberId = 1L;
            int year = 2024;
            int month = 7;

            // when
            List<ScheduleDTO> schedules = scheduleRepository.searchMonthlyInforms(keyword, MemberTeamRole.ADMIN, teamId, memberId, year, month);

            // then
            assertThat(schedules).isNotEmpty();
            assertThat(schedules).extracting("title").contains("공지 및 일정3");
            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
        }

        @Test
        @DisplayName("키워드에 해당하는 글이 없을 경우 빈 리스트를 반환한다")
        void testSearchMonthlyInformsWithNoKeyword() {
            // given
            String keyword = "[][[][][][][]";
            Long teamId = 1L;
            Long memberId = 1L;
            int year = 2024;
            int month = 8;

            // when
            List<ScheduleDTO> schedules = scheduleRepository.searchMonthlyInforms(keyword, MemberTeamRole.ORDERER, teamId, memberId, year, month);

            // then
            assertThat(schedules).isEmpty();
        }

        @Test
        @DisplayName("해당 월에 속하지 않는 일정을 검색하지 않는다")
        void testSearchMonthlyInformsWithDifferentMonth() {
            // given
            String keyword = "내용";
            Long teamId = 1L;
            Long memberId = 1L;
            int year = 2024;
            int month = 6;

            // when
            List<ScheduleDTO> schedules = scheduleRepository.searchMonthlyInforms(keyword, MemberTeamRole.ORDERER, teamId, memberId, year, month);

            // then
            assertThat(schedules).isEmpty();
        }
    }


    @Nested
    @DisplayName("findMonthlyOrders 메서드는")
    class FindMonthlyOrdersTests {

        @Test
        @DisplayName("관리자일 경우 팀의 해당 월의 주문을 모두 가져온다")
        void testFindMonthlyOrdersAdmin() {
            // given
            Long teamId = 1L;
            Long memberId = 1L;
            int year = 2024;
            int month = 7;
            MemberTeamRole role = MemberTeamRole.ADMIN;

            // when
            List<MonthlyScheduleDTO> schedules = scheduleRepository.findMonthlyOrders(role, teamId, memberId, year, month);

            // then
            assertThat(schedules).isNotEmpty();
            assertThat(schedules).extracting("title").contains(
                    "제품 A 서브제품1 10개",
                    "제품 A 서브제품1 5개",
                    "제품 B 서브제품2 20개",
                    "제품 A 서브제품1 12개",
                    "제품 B 서브제품2 8개",
                    "제품 A 서브제품1 18개");
            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
        }

        @Test
        @DisplayName("주문자일 경우 팀의 해당 월의 주문을 모두 가져온다")
        void testFindMonthlyOrdersOrderer() {
            // given
            Long teamId = 1L;
            Long memberId = 2L;
            int year = 2024;
            int month = 7;
            MemberTeamRole role = MemberTeamRole.ORDERER;

            // when
            List<MonthlyScheduleDTO> schedules = scheduleRepository.findMonthlyOrders(role, teamId, memberId, year, month);

            // then
            assertThat(schedules).isNotEmpty();
            assertThat(schedules).extracting("title").contains(
                    "제품 A 서브제품1 5개",
                    "제품 A 서브제품1 12개"
            );

            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
        }
    }





    @Nested
    @DisplayName("findDailyInforms는")
    class FindDailyInformsTests {

        @Test
        @DisplayName("특정 날짜의 일정을 가져온다")
        void testFindDailyInforms() {
            // given
            int year = 2024;
            int month = 7;
            int day = 26;
            Long memberId = 1L;
            Long teamId = 1L;

            // when
            List<ScheduleDTO> schedules = scheduleRepository.findDailyInforms(MemberTeamRole.ADMIN, teamId, memberId, year, month, day);

            // then
            assertThat(schedules).isNotEmpty();
            assertThat(schedules)
                    .extracting("title")
                    .contains("공지 및 일정1"); // Example title from the given data
        }
    }

    @Nested
    @DisplayName("findDailyOrders는")
    class FindDailyOrdersTests {

        @Test
        @DisplayName("특정 날짜의 주문을 가져온다")
        void testFindDailyOrders() {
            // given
            int year = 2024;
            int month = 7;
            int day = 26;
            Long memberId = 1L;
            Long teamId = 1L;

            // when
            List<ScheduleDTO> schedules = scheduleRepository.findDailyOrders(MemberTeamRole.ADMIN, teamId, memberId, year, month, day);

            // then
            assertThat(schedules).isNotEmpty();
        }
    }









}
