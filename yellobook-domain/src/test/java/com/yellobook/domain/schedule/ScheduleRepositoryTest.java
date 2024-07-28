package com.yellobook.domain.schedule;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.config.TestConfig;
import com.yellobook.domains.schedule.dto.QueryMonthlyScheduleDTO;
import com.yellobook.domains.schedule.dto.QueryScheduleDTO;
import com.yellobook.domains.schedule.dto.QueryUpcomingScheduleDTO;
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
    private ScheduleRepository scheduleRepository;
    // 오늘 날짜
    private final LocalDate today;
    // 서비스 사용자
    private final TeamMemberVO admin;
    private final TeamMemberVO orderer;
    private final TeamMemberVO viewer;
    private final Long INVALID_VALUE = 999999L;

    @Autowired
    public ScheduleRepositoryTest(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
        // today 를 고정해야 sql 파일 데이터 반영 가능
        this.today = LocalDate.of(2024, 7, 25);
        this.admin = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
        this.orderer = TeamMemberVO.of(2L, 1L, MemberTeamRole.ORDERER);
        this.viewer = TeamMemberVO.of(3L, 1L, MemberTeamRole.VIEWER);
    }

    @Nested
    @DisplayName("findEarliestOrder 는")
    class FindEarliestOrderTests {

        @Test
        @DisplayName("주문자라면 팀에서 자신이 주문한 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
        void testFindEarliestOrderForOrderer() {
            // given
            TeamMemberVO teamMember = orderer;
            // when
            Optional<QueryUpcomingScheduleDTO> result = scheduleRepository.findEarliestOrder(today, teamMember);

            // then
            assertThat(result).isPresent();
            QueryUpcomingScheduleDTO schedule = result.get();
            assertThat(schedule.getTitle()).isEqualTo("제품 A 서브제품1 5개");
            assertThat(schedule.getDay()).isEqualTo("2024-07-27");
            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.ORDER.getValue());
        }

        @Test
        @DisplayName("관리자라면 팀에서 모든 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
        void testFindEarliestOrderForAdmin() {
            // given
            TeamMemberVO teamMember = admin;

            // when
            Optional<QueryUpcomingScheduleDTO> result = scheduleRepository.findEarliestOrder(today, teamMember);

            // then
            assertThat(result).isPresent();
            QueryUpcomingScheduleDTO schedule = result.get();
            assertThat(schedule.getTitle()).isEqualTo("제품 A 서브제품1 10개");
            assertThat(schedule.getDay()).isEqualTo("2024-07-26");
            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.ORDER.getValue());
        }

        @Test
        @DisplayName("팀이 존재하지 않는다면 결과가 비어 있어야 한다.")
        void testFindEarliestOrderWithInvalidTeamId() {
            // given
            Long invalidTeamId = INVALID_VALUE;
            TeamMemberVO teamMember = TeamMemberVO.of(1L, invalidTeamId, MemberTeamRole.VIEWER);

            // when
            Optional<QueryUpcomingScheduleDTO> result = scheduleRepository.findEarliestOrder(today, teamMember);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("없는 주문자라면 결과가 비어 있어야 한다.")
        void testFindEarliestOrderWithInvalidMemberId() {
            // given
            Long invalidMemberId = INVALID_VALUE;
            TeamMemberVO teamMember = TeamMemberVO.of(invalidMemberId, 1L, MemberTeamRole.ORDERER);

            // when
            Optional<QueryUpcomingScheduleDTO> result = scheduleRepository.findEarliestOrder(today, teamMember);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findEarliestInform 는")
    class findEarliestInformTests {

        @Test
        @DisplayName("사용자와 관련된 가장 이른 공지 및 일정 을 가져와야 한다.")
        void testFindEarliestInformForMember() {
            // given
            TeamMemberVO teamMember = admin;

            // when
            Optional<QueryUpcomingScheduleDTO> result = scheduleRepository.findEarliestInform(today, teamMember);

            // then
            assertThat(result).isPresent();
            QueryUpcomingScheduleDTO schedule = result.get();
            assertThat(schedule.getTitle()).isEqualTo("공지 및 일정1");
            assertThat(schedule.getDay()).isEqualTo(LocalDate.of(2024, 7, 26));
            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.INFORM.getValue());
        }

        @Test
        @DisplayName("팀 구성원이 아닌 경우 결과가 없어야 한다.")
        void testFindEarliestInformForNonTeamMember() {
            // given
            Long invalidTeamMemberId = INVALID_VALUE;
            TeamMemberVO teamMember = TeamMemberVO.of(invalidTeamMemberId, 1L, MemberTeamRole.ADMIN);

            // when
            Optional<QueryUpcomingScheduleDTO> result = scheduleRepository.findEarliestInform(today, teamMember);

            // then
            assertThat(result).isNotPresent();
        }

        @Test
        @DisplayName("태그된 일정이 존재하면 해당 일정도 가져와야 한다.")
        void testFindEarliestInformForMentionedMember() {
            // given
            TeamMemberVO teamMember = orderer;

            // when
            Optional<QueryUpcomingScheduleDTO> result = scheduleRepository.findEarliestInform(today, teamMember);

            // then
            assertThat(result).isPresent();
            QueryUpcomingScheduleDTO schedule = result.get();
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
            TeamMemberVO teamMember = admin;
            int year = 2024;
            int month = 7;

            // when
            List<QueryScheduleDTO> result = scheduleRepository.searchMonthlyOrders(keyword, year, month, teamMember);

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
            TeamMemberVO teamMember = admin;
            int year = 2024;
            int month = 7;

            // when
            List<QueryScheduleDTO> result = scheduleRepository.searchMonthlyOrders(keyword, year, month, teamMember);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("특정 키워드와 연관된 주문 목록을 올바르게 정렬해야 한다.")
        void testSearchMonthlyOrdersSortedOrder() {
            // given
            String keyword = "제품";
            TeamMemberVO teamMember = admin;
            int year = 2024;
            int month = 7;

            // when
            List<QueryScheduleDTO> result = scheduleRepository.searchMonthlyOrders(keyword, year, month, teamMember);

            // then
            assertThat(result).isNotEmpty();
            assertThat(result).isSortedAccordingTo(Comparator.comparing(QueryScheduleDTO::getDate));
        }
    }

    @Nested
    @DisplayName("searchMonthlyInforms는")
    class searchMonthlyInformsTests {

        @Test
        @DisplayName("자신이 작성한 일정을 검색할 수 있어야 한다.")
        void testSearchMonthlyInformsForOwn() {
            // given
            String keyword = "일정";
            TeamMemberVO teamMember = admin;
            int year = 2024;
            int month = 7;

            // when
            List<QueryScheduleDTO> schedules = scheduleRepository.searchMonthlyInforms(keyword, year, month, teamMember);

            // then
            assertThat(schedules).isNotEmpty();
            assertThat(schedules).extracting("title").contains("공지 및 일정7");
            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
        }

        @Test
        @DisplayName("자신이 태그된 일정을 검색할 수 있다.")
        void testSearchMonthlyInformsForTagged() {
            // given
            String keyword = "일정";
            TeamMemberVO teamMember = admin;
            int year = 2024;
            int month = 7;

            // when
            List<QueryScheduleDTO> schedules = scheduleRepository.searchMonthlyInforms(keyword, year, month, teamMember);

            // then
            assertThat(schedules).isNotEmpty();
            assertThat(schedules).extracting("title").contains("공지 및 일정3");
            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
        }

        @Test
        @DisplayName("키워드에 해당하는 글이 없을 경우 빈 리스트를 반환한다.")
        void testSearchMonthlyInformsWithNoKeyword() {
            // given
            String keyword = "[][[][][][][]";
            TeamMemberVO teamMember = admin;
            int year = 2024;
            int month = 8;

            // when
            List<QueryScheduleDTO> schedules = scheduleRepository.searchMonthlyInforms(keyword, year, month, teamMember);

            // then
            assertThat(schedules).isEmpty();
        }

        @Test
        @DisplayName("해당 월에 속하지 않는 일정을 검색하지 않는다.")
        void testSearchMonthlyInformsWithDifferentMonth() {
            // given
            String keyword = "내용";
            TeamMemberVO teamMember = admin;
            int year = 2024;
            int month = 6;

            // when
            List<QueryScheduleDTO> schedules = scheduleRepository.searchMonthlyInforms(keyword, year, month, teamMember);

            // then
            assertThat(schedules).isEmpty();
        }
    }


    @Nested
    @DisplayName("findMonthlyOrders 는")
    class FindMonthlyOrdersTests {

        @Test
        @DisplayName("관리자일 경우 팀의 해당 월의 주문을 모두 가져와야 한다.")
        void testFindMonthlyOrdersAdmin() {
            // given
            TeamMemberVO teamMember = admin;
            int year = 2024;
            int month = 7;
            MemberTeamRole role = MemberTeamRole.ADMIN;

            // when
            List<QueryMonthlyScheduleDTO> schedules = scheduleRepository.findMonthlyOrders(year, month, teamMember);

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
        @DisplayName("주문자일 경우 팀의 해당 월의 주문을 모두 가져와야 한다.")
        void testFindMonthlyOrdersOrderer() {
            // given
            TeamMemberVO teamMember = orderer;
            int year = 2024;
            int month = 7;
            MemberTeamRole role = MemberTeamRole.ORDERER;

            // when
            List<QueryMonthlyScheduleDTO> schedules = scheduleRepository.findMonthlyOrders(year, month, teamMember);

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
    @DisplayName("findMonthlyInforms 는")
    class FindMonthlyInformsTests {

        @Test
        @DisplayName("사용자와 연관된 월의 공지 및 일정을 모두 가져와야 한다.")
        void testFindMonthlyOrdersAdmin() {
            // given
            TeamMemberVO teamMember = admin;
            int year = 2024;
            int month = 7;
            MemberTeamRole role = MemberTeamRole.ADMIN;

            // when
            List<QueryMonthlyScheduleDTO> schedules = scheduleRepository.findMonthlyOrders(year, month, teamMember);

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
    }


    @Nested
    @DisplayName("findDailyInforms는")
    class FindDailyInformsTests {

        @Test
        @DisplayName("특정 날짜의 일정을 가져와야 한다.")
        void testFindDailyInforms() {
            // given
            int year = 2024;
            int month = 7;
            int day = 26;
            TeamMemberVO teamMember = admin;

            // when
            List<QueryScheduleDTO> schedules = scheduleRepository.findDailyInforms(year, month, day, teamMember);

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
        @DisplayName("특정 날짜의 주문을 가져와야 한다.")
        void testFindDailyOrders() {
            // given
            int year = 2024;
            int month = 7;
            int day = 26;
            TeamMemberVO teamMember = admin;

            // when
            List<QueryScheduleDTO> schedules = scheduleRepository.findDailyOrders(year, month, day, teamMember);

            // then
            assertThat(schedules).isNotEmpty();
        }
    }




}
