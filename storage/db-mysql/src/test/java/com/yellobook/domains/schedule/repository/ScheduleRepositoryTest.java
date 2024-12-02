package com.yellobook.domains.schedule.repository;

import static fixture.InformFixture.createInform;
import static fixture.InformFixture.createInformMention;
import static fixture.InventoryFixture.createInventory;
import static fixture.InventoryFixture.createProduct;
import static fixture.MemberFixture.createMember;
import static fixture.OrderFixture.createOrderMention;
import static fixture.TeamFixture.createParticipant;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.schedule.dto.DailyCond;
import com.yellobook.domains.schedule.dto.EarliestCond;
import com.yellobook.domains.schedule.dto.MonthlyCond;
import com.yellobook.domains.schedule.dto.SearchMonthlyCond;
import com.yellobook.domains.schedule.dto.query.QueryMonthlySchedule;
import com.yellobook.domains.schedule.dto.query.QuerySchedule;
import com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.ScheduleType;
import com.yellobook.TeamMemberRole;
import com.yellobook.support.RepositoryTest;
import fixture.OrderFixture;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ScheduleRepository Unit Test")
public class ScheduleRepositoryTest extends RepositoryTest {
    @Autowired
    protected ScheduleRepository scheduleRepository;

    private final TeamMemberVO adminVo = TeamMemberVO.of(1L, 1L, TeamMemberRole.ADMIN);
    private final TeamMemberVO ordererVo = TeamMemberVO.of(2L, 1L, TeamMemberRole.ORDERER);
    private final TeamMemberVO viewerVo = TeamMemberVO.of(3L, 1L, TeamMemberRole.VIEWER);
    private final LocalDate today = LocalDate.of(2024, 9, 1);
    private final int ORDERER_MONTHLY_ORDERS_COUNT = 30;
    private final int ADMIN_MONTHLY_ORDER_MENTIONS_COUNT = 30;
    private final int ADMIN_MONTHLY_INFORMS_COUNT = 0;
    private final int ADMIN_MONTHLY_INFORM_MENTIONS_COUNT = 30;
    private final int ORDERER_MONTHLY_INFORMS_COUNT = 30;
    private final int ORDERER_MONTHLY_INFORM_MENTIONS_COUNT = 0;
    private final int VIEWER_MONTHLY_INFORMS_COUNT = 30;
    private final int VIEWER_MONTHLY_INFORM_MENTIONS_COUNT = 30;

    @BeforeEach
    void setUp() {
        resetAutoIncrement();
        initData();
    }

    @Nested
    @DisplayName("데이터베이스 초기화 테스트")
    class initTest {
        @Test
        @DisplayName("사용자는 총 3명이어야 한다.")
        void testMemberCount() {
            Long memberCount = getEntityCount(Member.class);
            assertEquals(3, memberCount);
        }

        @Test
        @DisplayName("주문은 총 40건이어야 한다.")
        void testOrderCount() {
            Long orderCount = getEntityCount(Order.class);
            assertEquals(40, orderCount);
        }

        @Test
        @DisplayName("공지는 총 80개여야 한다.")
        void testInformCount() {
            Long informCount = getEntityCount(Inform.class);
            assertEquals(80, informCount);
        }

        @Test
        @DisplayName("관리자가 멘션된 이번달 주문은 30건이어야 한다.")
        void testMonthlyOrderMentionCountForAdmin() {
            Long orderCount = getMonthlyOrderMentionCountByTeamMemberRole(TeamMemberRole.ADMIN, today);
            assertEquals(ADMIN_MONTHLY_ORDER_MENTIONS_COUNT, orderCount);
        }

        @Test
        @DisplayName("관리자가 멘션된 이번달 공지는 30건이어야 한다.")
        void testMonthlyMentionCountForAdmin() {
            Long informCount = getMonthlyInformMentionCountByTeamMemberRole(TeamMemberRole.ADMIN, today);
            assertEquals(ADMIN_MONTHLY_INFORM_MENTIONS_COUNT, informCount);
        }

        @Test
        @DisplayName("주문자가 멘션된 이번달 공지는 없어야 한다.")
        void testMonthlyMentionCountForOrderer() {
            Long informCount = getMonthlyOrderMentionCountByTeamMemberRole(TeamMemberRole.ORDERER, today);
            assertEquals(ORDERER_MONTHLY_INFORM_MENTIONS_COUNT, informCount);
        }

        @Test
        @DisplayName("뷰어가 멘션된 이번달 공지는 30건이어야 한다.")
        void testMonthlyMentionCountForViewer() {
            Long informCount = getMonthlyInformMentionCountByTeamMemberRole(TeamMemberRole.VIEWER, today);
            assertEquals(VIEWER_MONTHLY_INFORM_MENTIONS_COUNT, informCount);
        }
    }

    @Nested
    @DisplayName("findEarliestOrder 메서드는")
    class Describe_findEarliestOrder {

        @Nested
        @DisplayName("주문자라면")
        class Context_orderer {
            final EarliestCond cond = EarliestCond.builder()
                    .teamMember(ordererVo)
                    .today(today)
                    .build();
            QueryUpcomingSchedule schedule;

            @BeforeEach
            void setUpContext() {
                schedule = scheduleRepository.findEarliestOrder(cond)
                        .orElseThrow(() -> new IllegalStateException("주문자의 주문이 없습니다."));
            }

            @Test
            @DisplayName("자신이 주문한 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
            void it_returns_earliest_order_starting_from_tomorrow_that_user_placed() {
                assertThat(schedule.title()).isEqualTo("제품 서브제품 1개");
                assertThat(schedule.day()).isEqualTo("2024-09-02");
                Assertions.assertThat(schedule.scheduleType())
                        .isEqualTo(ScheduleType.ORDER);
            }
        }

        @Nested
        @DisplayName("관리자라면")
        class Context_admin {
            final EarliestCond cond = EarliestCond.builder()
                    .teamMember(adminVo)
                    .today(today)
                    .build();

            private QueryUpcomingSchedule schedule;

            @BeforeEach
            void setUpContext() {
                schedule = scheduleRepository.findEarliestOrder(cond)
                        .orElseThrow(() -> new IllegalStateException("관리자가 확인할 수 있는 주문이 없습니다."));
            }

            @Test
            @DisplayName("팀에서 모든 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
            void it_returns_earliest_order_starting_from_tomorrow_for_the_team() {
                assertThat(schedule.title()).isEqualTo("제품 서브제품 1개");
                assertThat(schedule.day()).isEqualTo("2024-09-02");
                Assertions.assertThat(schedule.scheduleType())
                        .isEqualTo(ScheduleType.ORDER);
            }
        }
    }

    @Nested
    @DisplayName("findEarliestInform 메서드는")
    class Describe_findEarliestInform {

        @Nested
        @DisplayName("사용자와 관련된 공지가 존재한다면")
        class Context_with_related_user {
            final EarliestCond cond = EarliestCond.builder()
                    .today(today)
                    .teamMember(viewerVo)
                    .build();
            private QueryUpcomingSchedule schedule;

            @BeforeEach
            void setUpContext() {
                schedule = scheduleRepository.findEarliestInform(cond)
                        .orElseThrow(() -> new IllegalStateException("공지가 없습니다."));
            }

            @Test
            @DisplayName("내일부터 가장 이른 공지를 반환해야 한다.")
            void it_returns_earliest_inform_starting_from_tomorrow() {
                assertThat(schedule.title()).isEqualTo("공지");
                assertThat(schedule.day()).isEqualTo("2024-09-02");
                Assertions.assertThat(schedule.scheduleType())
                        .isEqualTo(ScheduleType.INFORM);
            }
        }
    }

    @Nested
    @DisplayName("searchMonthlyOrders 메서드는")
    class Describe_searchMonthlyOrders {

        @Nested
        @DisplayName("관리자라면")
        class Context_with_admin {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final SearchMonthlyCond cond = SearchMonthlyCond.builder()
                    .keyword("제품")
                    .teamMember(adminVo)
                    .year(year)
                    .month(month)
                    .build();
            private List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyOrders(cond);
            }

            @Test
            @DisplayName("이번달 모든 주문을 가져와야 한다.")
            void it_returns_all_monthly_orders_for_admin() {
                assertThat(schedules.size()).isEqualTo(ADMIN_MONTHLY_ORDER_MENTIONS_COUNT);
                schedules.forEach(schedule -> {
                    Assertions.assertThat(schedule.scheduleType())
                            .isEqualTo(ScheduleType.ORDER);
                    assertThat(schedule.date()
                            .getYear()).isEqualTo(today.getYear());
                    assertThat(schedule.date()
                            .getMonthValue()).isEqualTo(today.getMonthValue());
                });
            }
        }


        @Nested
        @DisplayName("주문자라면")
        class Context_with_orderer {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final SearchMonthlyCond cond = SearchMonthlyCond.builder()
                    .keyword("제품")
                    .teamMember(ordererVo)
                    .year(year)
                    .month(month)
                    .build();
            private List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyOrders(cond);
            }

            @Test
            @DisplayName("이번달 본인이 주문한 주문들을 가져와야 한다.")
            void it_returns_all_monthly_orders_for_orderer() {
                assertThat(schedules.size()).isEqualTo(ORDERER_MONTHLY_ORDERS_COUNT);
                schedules.forEach(schedule -> {
                    Assertions.assertThat(schedule.scheduleType())
                            .isEqualTo(ScheduleType.ORDER);
                    assertThat(schedule.date()
                            .getYear()).isEqualTo(today.getYear());
                    assertThat(schedule.date()
                            .getMonthValue()).isEqualTo(today.getMonthValue());
                });
            }
        }

        @Nested
        @DisplayName("키워드가 존재하지 않는다면")
        class Context_with_invalid_keyword {
            final SearchMonthlyCond cond = SearchMonthlyCond.builder()
                    .keyword("없는키워드")
                    .teamMember(ordererVo)
                    .year(today.getYear())
                    .month(today.getMonthValue())
                    .build();
            private List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyOrders(cond);
            }

            @Test
            @DisplayName("빈 리스트를 반환해야 한다.")
            void it_returns_empty_list_for_invalid_keyword() {
                assertThat(schedules).isEmpty();
            }
        }

        @Nested
        @DisplayName("검색결과에 맞는 주문들이 존재한다면")
        class Context_exist_order {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final SearchMonthlyCond cond = SearchMonthlyCond.builder()
                    .keyword("제품")
                    .teamMember(ordererVo)
                    .year(year)
                    .month(month)
                    .build();

            List<QuerySchedule> schedule;

            @BeforeEach
            void setUpContext() {
                schedule = scheduleRepository.searchMonthlyOrders(cond);
            }

            @Test
            @DisplayName("이를 날짜가 빠른 순으로 정렬해서 반환해야 한다.")
            void it_returns_sorted_orders() {
                assertThat(schedule).isNotEmpty();
                assertThat(schedule).isSortedAccordingTo(Comparator.comparing(QuerySchedule::date));
            }
        }
    }

    @Nested
    @DisplayName("searchMonthlyInforms 메서드는")
    class Describe_searchMonthlyInforms {

        @Nested
        @DisplayName("자신이 작성한 공지목록과 멘션된 공지 목록이 모두 존재할 경우")
        class Context_with_written_and_mentioned_informs {
            final SearchMonthlyCond cond = SearchMonthlyCond.builder()
                    .keyword("공지")
                    .teamMember(viewerVo)
                    .year(today.getYear())
                    .month(today.getMonthValue())
                    .build();

            List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyInforms(cond);
            }

            @Test
            @DisplayName("해당하는 공지들을 반환해야 한다.")
            void it_returns_all_relevant_informs() {
                assertThat(schedules.size()).isEqualTo(
                        VIEWER_MONTHLY_INFORMS_COUNT + VIEWER_MONTHLY_INFORM_MENTIONS_COUNT);
            }
        }

        @Nested
        @DisplayName("자신이 작성한 공지 목록만 존재할 경우")
        class Context_with_only_written_informs {
            final SearchMonthlyCond cond = SearchMonthlyCond.builder()
                    .keyword("공지")
                    .teamMember(ordererVo)
                    .year(today.getYear())
                    .month(today.getMonthValue())
                    .build();

            List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyInforms(cond);
            }

            @Test
            @DisplayName("해당하는 공지들을 반환해야 한다.")
            void it_returns_only_written_informs() {
                assertThat(schedules.size()).isEqualTo(ORDERER_MONTHLY_INFORMS_COUNT);
            }
        }

        @Nested
        @DisplayName("자신이 멘션된 공지 목록만 존재할 경우")
        class Context_with_only_mentioned_informs {
            final SearchMonthlyCond cond = SearchMonthlyCond.builder()
                    .keyword("공지")
                    .teamMember(adminVo)
                    .year(today.getYear())
                    .month(today.getMonthValue())
                    .build();

            List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyInforms(cond);
            }

            @Test
            @DisplayName("해당하는 공지들을 반환해야 한다.")
            void it_returns_only_mentioned_informs() {
                assertThat(schedules.size()).isEqualTo(ADMIN_MONTHLY_INFORM_MENTIONS_COUNT);
            }
        }
    }

    @Nested
    @DisplayName("findMonthlyOrders 메서드는")
    class Describe_findMonthlyOrders {

        @Nested
        @DisplayName("관리자일 경우")
        class Context_with_admin {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final MonthlyCond cond = MonthlyCond.builder()
                    .teamMember(adminVo)
                    .year(year)
                    .month(month)
                    .build();
            private List<QueryMonthlySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.findMonthlyOrders(cond);
            }

            @Test
            @DisplayName("팀의 해당 월의 모든 주문을 가져와야 한다.")
            void it_returns_all_monthly_orders_for_admin() {
                assertThat(schedules.size()).isEqualTo(ADMIN_MONTHLY_ORDER_MENTIONS_COUNT);
            }
        }

        @Nested
        @DisplayName("주문자일 경우")
        class Context_with_orderer {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final MonthlyCond cond = MonthlyCond.builder()
                    .teamMember(ordererVo)
                    .year(year)
                    .month(month)
                    .build();
            private List<QueryMonthlySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.findMonthlyOrders(cond);
            }

            @Test
            @DisplayName("팀의 해당 월의 자신이 주문한 주문을 모두 가져와야 한다.")
            void it_returns_all_monthly_orders_for_orderer() {
                assertThat(schedules.size()).isEqualTo(ORDERER_MONTHLY_ORDERS_COUNT);
            }
        }
    }

    @Nested
    @DisplayName("findMonthlyInforms 메서드는")
    class Describe_findMonthlyInforms {

        abstract class ContextTeamMember {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            private List<QueryMonthlySchedule> schedules;

            abstract TeamMemberVO getTeamMember();

            abstract int expectedScheduleSize();

            @BeforeEach
            void setUpContext() {
                MonthlyCond cond = MonthlyCond.builder()
                        .teamMember(getTeamMember())
                        .year(year)
                        .month(month)
                        .build();
                schedules = scheduleRepository.findMonthlyInforms(cond);
            }

            @Test
            @DisplayName("사용자가 작성했거나 태그한 월의 공지들을 모두 가져와야 한다.")
            void it_returns_all_monthly_informs() {
                assertThat(schedules.size()).isEqualTo(expectedScheduleSize());
            }
        }

        @Nested
        @DisplayName("뷰어일 경우")
        class Context_with_viewer extends ContextTeamMember {
            @Override
            TeamMemberVO getTeamMember() {
                return viewerVo;
            }

            @Override
            int expectedScheduleSize() {
                return VIEWER_MONTHLY_INFORMS_COUNT + VIEWER_MONTHLY_INFORM_MENTIONS_COUNT;
            }
        }

        @Nested
        @DisplayName("관리자일 경우")
        class Context_with_admin extends ContextTeamMember {
            @Override
            TeamMemberVO getTeamMember() {
                return adminVo;
            }

            @Override
            int expectedScheduleSize() {
                return ADMIN_MONTHLY_INFORMS_COUNT + ADMIN_MONTHLY_INFORM_MENTIONS_COUNT;
            }
        }

        @Nested
        @DisplayName("주문자일 경우")
        class Context_with_orderer extends ContextTeamMember {
            @Override
            TeamMemberVO getTeamMember() {
                return ordererVo;
            }

            @Override
            int expectedScheduleSize() {
                return ORDERER_MONTHLY_INFORMS_COUNT + ORDERER_MONTHLY_INFORM_MENTIONS_COUNT;
            }
        }
    }

    @Nested
    @DisplayName("findDailyInforms 메서드는")
    class Describe_findDailyInforms {

        abstract class ContextTeamMember {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final int day = today.getDayOfMonth();
            private List<QuerySchedule> schedules;

            abstract TeamMemberVO getTeamMember();

            abstract int expectedScheduleSize();

            @BeforeEach
            void setUpContext() {
                DailyCond cond = DailyCond.builder()
                        .year(year)
                        .month(month)
                        .day(day)
                        .teamMember(getTeamMember())
                        .build();
                schedules = scheduleRepository.findDailyInforms(cond);
            }

            @Test
            @DisplayName("작성했거나 태그된 오늘 날짜의 공지 목록을 반환해야 한다.")
            void it_returns_daily_informs() {
                assertThat(schedules.size()).isEqualTo(expectedScheduleSize());
            }
        }

        @Nested
        @DisplayName("뷰어일 경우")
        class Context_with_viewer extends ContextTeamMember {
            @Override
            TeamMemberVO getTeamMember() {
                return viewerVo;
            }

            @Override
            int expectedScheduleSize() {
                return 2;
            }
        }

        @Nested
        @DisplayName("관리자일 경우")
        class Context_with_admin extends ContextTeamMember {
            @Override
            TeamMemberVO getTeamMember() {
                return adminVo;
            }

            @Override
            int expectedScheduleSize() {
                return 1;
            }
        }

        @Nested
        @DisplayName("주문자일 경우")
        class Context_with_orderer extends ContextTeamMember {
            @Override
            TeamMemberVO getTeamMember() {
                return ordererVo;
            }

            @Override
            int expectedScheduleSize() {
                return 1;
            }
        }
    }

    @Nested
    @DisplayName("findDailyOrders 메서드는")
    class Describe_findDailyOrders {

        @Nested
        @DisplayName("특정 날짜가 주어진 경우")
        class Context_with_specific_date {
            final DailyCond cond = DailyCond.builder()
                    .year(today.getYear())
                    .month(today.getMonthValue())
                    .day(today.getDayOfMonth())
                    .teamMember(ordererVo)
                    .build();
            private List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.findDailyOrders(cond);
            }

            @Test
            @DisplayName("주문목록을 반환해야 한다.")
            void it_returns_daily_orders() {
                assertThat(schedules.size()).isEqualTo(1);
            }
        }
    }

    private void initData() {
        Team team = em.persistAndFlush(createTeam("팀1"));

        Member admin = em.persistAndFlush(createMember());
        Member orderer = em.persistAndFlush(createMember());
        Member viewer = em.persistAndFlush(createMember());

        em.persistAndFlush(createParticipant(team, admin, TeamMemberRole.ADMIN));
        em.persistAndFlush(createParticipant(team, orderer, TeamMemberRole.ORDERER));
        em.persistAndFlush(createParticipant(team, viewer, TeamMemberRole.VIEWER));

        Inventory inventory = em.persistAndFlush(createInventory(team));

        Product product = em.persistAndFlush(createProduct(inventory));

        for (int i = 0; i < 40; i++) {
            LocalDate date = today.plusDays(i);
            Order order = em.persistAndFlush(OrderFixture.createOrder(team, orderer, product, date));
            em.persistAndFlush(createOrderMention(order, admin));
        }

        for (int i = 0; i < 40; i++) {
            LocalDate date = today.plusDays(i);
            Inform inform = em.persistAndFlush(createInform(team, viewer, date));
            em.persistAndFlush(createInformMention(inform, admin));
        }

        for (int i = 0; i < 40; i++) {
            LocalDate date = today.plusDays(i);
            Inform inform = em.persistAndFlush(createInform(team, orderer, date));
            em.persistAndFlush(createInformMention(inform, viewer));
        }
    }

    private Long getEntityCount(Class<?> entityType) {
        return em.getEntityManager()
                .createQuery("SELECT COUNT(e) FROM " + entityType.getSimpleName() + " e",
                        Long.class)
                .getSingleResult();
    }

    private Long getMonthlyInformMentionCountByTeamMemberRole(TeamMemberRole teamMemberRole, LocalDate date) {
        return em.getEntityManager()
                .createQuery(
                        "SELECT COUNT(im) FROM Inform i "
                                + "JOIN InformMention im ON i.id = im.inform.id "
                                + "JOIN Participant p ON p.member.id = im.member.id  "
                                + "WHERE p.teamMemberRole = :teamMemberRole AND YEAR(i.date) = :year AND MONTH(i.date) = :month",
                        Long.class)
                .setParameter("teamMemberRole", teamMemberRole)
                .setParameter("year", date.getYear())
                .setParameter("month", date.getMonthValue())
                .getSingleResult();
    }

    private Long getMonthlyOrderMentionCountByTeamMemberRole(TeamMemberRole teamMemberRole, LocalDate date) {
        return em.getEntityManager()
                .createQuery(
                        "SELECT COUNT(om) FROM Order o "
                                + "JOIN OrderMention om ON o.id = om.order.id "
                                + "JOIN Participant p ON p.member.id = om.member.id  "
                                + "WHERE p.teamMemberRole = :teamMemberRole AND YEAR(o.date) = :year AND MONTH(o.date) = :month",
                        Long.class)
                .setParameter("teamMemberRole", teamMemberRole)
                .setParameter("year", date.getYear())
                .setParameter("month", date.getMonthValue())
                .getSingleResult();
    }
}



