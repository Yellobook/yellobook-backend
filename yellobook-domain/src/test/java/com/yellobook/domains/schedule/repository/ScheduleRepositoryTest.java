package com.yellobook.domains.schedule.repository;

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
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.ScheduleType;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.support.annotation.RepositoryTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import static fixture.InformFixture.createInform;
import static fixture.InformFixture.createInformMention;
import static fixture.InventoryFixture.createInventory;
import static fixture.InventoryFixture.createProduct;
import static fixture.MemberFixture.createMember;
import static fixture.OrderFixture.createOrder;
import static fixture.OrderFixture.createOrderMention;
import static fixture.TeamFixture.createParticipant;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RepositoryTest
@DisplayName("ScheduleRepository 테스트")
public class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @PersistenceContext
    EntityManager em;

    // 오늘 날짜
    private final LocalDate today = LocalDate.of(2024, 8, 3);

    // 서비스 사용자
    private final TeamMemberVO admin1 = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
    private final TeamMemberVO orderer1 = TeamMemberVO.of(2L, 1L, MemberTeamRole.ORDERER);
    private final TeamMemberVO orderer2 = TeamMemberVO.of(3L, 1L, MemberTeamRole.ORDERER);
    private final TeamMemberVO viewer1 = TeamMemberVO.of(4L, 1L, MemberTeamRole.VIEWER);

    // 날짜 포맷
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime baseTime = LocalDateTime.parse("2024-08-03 08:00:00", formatter);

    /**
     * 테스트 데이터
     * <p>
     * 오늘 날짜: 8월 3일
     * 주문자1: 8.3, 8.5, 8.6 ... 총 주문 10개 (2일 간격)
     * 주문자2: 8.4, 8.6 ... 총 주문 10개 (2일 간격)
     * 뷰어1: 8.3, 8.4, 8.5 ... 총 공지 및 일정 10개 (1일 간격)
     * 관리자: 태그된 공지 및 일정 총 10개 (뷰어1이 작성한 모든 공지 및 일정에 태그)
     */
    @BeforeEach
    void setUp() throws Exception {
        initData();
    }

    @Nested
    @DisplayName("데이터베이스 초기화 테스트")
    class initTests {
        @Test
        @DisplayName("사용자는 총 4명이어야 한다.")
        void testMemberCount() {
            Long memberCount = em.createQuery("SELECT COUNT(m) FROM Member m", Long.class).getSingleResult();
            assertEquals(4, memberCount);
        }

        @Test
        @DisplayName("주문은 총 20건이어야 한다.")
        void testOrderCount() {
            Long orderCount = em.createQuery("SELECT COUNT(o) FROM Order o", Long.class).getSingleResult();
            assertEquals(20, orderCount);
        }

        @Test
        @DisplayName("공지 및 일정은 총 10건이어야 한다.")
        void testInformCount() {
            Long informCount = em.createQuery("SELECT COUNT(i) FROM Inform i", Long.class).getSingleResult();
            assertEquals(10, informCount);
        }
    }

    @Nested
    @DisplayName("findEarliestOrder 메소드는")
    class Describe_findEarliestOrder {

        @Nested
        @DisplayName("주문자라면")
        class Context_orderer {
            final EarliestCond cond = EarliestCond.builder().teamMember(orderer1).today(today).build();
            QueryUpcomingSchedule schedule;

            @BeforeEach
            void setUpContext() {
                schedule = scheduleRepository.findEarliestOrder(cond).orElseThrow(() -> new IllegalStateException("주문자의 주문이 없습니다."));
            }

            @Test
            @DisplayName("자신이 주문한 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
            void it_returns_earliest_order_starting_from_tomorrow_that_user_placed() {
                assertThat(schedule.title()).isEqualTo("제품 서브제품 1개");
                assertThat(schedule.day()).isEqualTo("2024-08-05");
                assertThat(schedule.scheduleType()).isEqualTo(ScheduleType.ORDER);
            }
        }

        @Nested
        @DisplayName("관리자라면")
        class Context_admin {
            final EarliestCond cond = EarliestCond.builder().teamMember(admin1).today(today).build();

            private QueryUpcomingSchedule schedule;

            @BeforeEach
            void setUpContext() {
                schedule = scheduleRepository.findEarliestOrder(cond).orElseThrow(() -> new IllegalStateException("관리자가 확인할 수 있는 주문이 없습니다."));
            }

            @Test
            @DisplayName("팀에서 모든 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
            void it_returns_earliest_order_starting_from_tomorrow_for_the_team() {
                assertThat(schedule.title()).isEqualTo("제품 서브제품 1개");
                assertThat(schedule.day()).isEqualTo("2024-08-04");
                assertThat(schedule.scheduleType()).isEqualTo(ScheduleType.ORDER);
            }
        }
    }

    @Nested
    @DisplayName("findEarliestInform 메소드는")
    class findEarliestInformTests {

        @Nested
        @DisplayName("사용자와 관련된 공지 및 일정이 존재한다면")
        class Context_relatedUser {
            final EarliestCond cond = EarliestCond.builder().today(today).teamMember(viewer1).build();
            private QueryUpcomingSchedule schedule;

            @BeforeEach
            void setUpContext() {
                schedule = scheduleRepository.findEarliestInform(cond).orElseThrow(() -> new IllegalStateException("공지 및 일정이 없습니다."));
            }

            @Test
            @DisplayName("내일부터 가장 이른 공지를 가져와야 한다.")
            void it_returns_earliest_inform_starting_from_tomorrow() {
                assertThat(schedule.title()).isEqualTo("공지 및 일정");
                assertThat(schedule.day()).isEqualTo("2024-08-04");
                assertThat(schedule.scheduleType()).isEqualTo(ScheduleType.INFORM);
            }
        }
    }


    @Nested
    @DisplayName("searchMonthlyOrders 메소드는")
    class searchMonthlyOrdersTests {

        @Nested
        @DisplayName("관리자라면")
        class Context_admin {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final SearchMonthlyCond cond = SearchMonthlyCond.builder().keyword("제품").teamMember(admin1).year(year).month(month).build();
            private List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyOrders(cond);
            }

            @Test
            @DisplayName("이번달 모든 주문을 가져와야 한다.")
            void it_returns_all_monthly_orders_for_admin() {
                assertThat(schedules.size()).isEqualTo(20);
                schedules.forEach(schedule -> {
                    assertThat(schedule.scheduleType()).isEqualTo(ScheduleType.ORDER);
                    assertThat(schedule.date().getYear()).isEqualTo(today.getYear());
                    assertThat(schedule.date().getMonthValue()).isEqualTo(today.getMonthValue());
                });
            }
        }

        @Nested
        @DisplayName("주문자라면")
        class Context_orderer {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final SearchMonthlyCond cond = SearchMonthlyCond.builder().keyword("제품").teamMember(orderer1).year(year).month(month).build();
            private List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyOrders(cond);
            }

            @Test
            @DisplayName("이번달 본인이 주문한 주문을 가져와야 한다.")
            void it_returns_all_monthly_orders_for_orderer() {
                assertThat(schedules.size()).isEqualTo(10);
                schedules.forEach(schedule -> {
                    assertThat(schedule.scheduleType()).isEqualTo(ScheduleType.ORDER);
                    assertThat(schedule.date().getYear()).isEqualTo(today.getYear());
                    assertThat(schedule.date().getMonthValue()).isEqualTo(today.getMonthValue());
                });
            }
        }

        @Nested
        @DisplayName("키워드가 존재하지 않는다면")
        class Context_invalid_keyword {
            final SearchMonthlyCond cond = SearchMonthlyCond.builder().keyword("없는키워드").teamMember(orderer1).year(today.getYear()).month(today.getMonthValue()).build();
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
        @DisplayName("검색결과에 맞는 일정들이 존재한다면")
        class Context_SortedOrders {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final SearchMonthlyCond cond = SearchMonthlyCond.builder().keyword("제품").teamMember(orderer1).year(year).month(month).build();

            List<QuerySchedule> schedule;

            @BeforeEach
            void setUpContext() {
                schedule = scheduleRepository.searchMonthlyOrders(cond);
            }

            @Test
            @DisplayName("이를 정렬해서 반환해야 한다.")
            void it_returns_sorted_orders() {
                assertThat(schedule).isNotEmpty();
                assertThat(schedule).isSortedAccordingTo(Comparator.comparing(QuerySchedule::date));
            }
        }
    }

    @Nested
    @DisplayName("searchMonthlyInforms 메소드는")
    class searchMonthlyInformsTests {

        @Nested
        @DisplayName("자신이 작성한 일정 목록이 존재한다면")
        class Context_Written {
            final SearchMonthlyCond cond = SearchMonthlyCond.builder().keyword("일정").teamMember(viewer1).year(today.getYear()).month(today.getMonthValue()).build();

            List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyInforms(cond);
            }

            @Test
            @DisplayName("이를 반환해야 한다.")
            void it_returns_written_informs() {
                assertThat(schedules.size()).isEqualTo(10);
            }
        }

        @Nested
        @DisplayName("자신이 멘션된 일정 목록이 존재한다면")
        class Context_Mentioned {
            final SearchMonthlyCond cond = SearchMonthlyCond.builder().keyword("일정").teamMember(admin1).year(today.getYear()).month(today.getMonthValue()).build();

            List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.searchMonthlyInforms(cond);
            }

            @Test
            @DisplayName("이를 반환해야 한다.")
            void it_returns_mentioned_informs() {
                assertThat(schedules.size()).isEqualTo(10);
            }
        }
    }


    @Nested
    @DisplayName("findMonthlyOrders 메소드는")
    class findMonthlyOrdersTests {

        @Nested
        @DisplayName("관리자일 경우")
        class Context_admin {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final MonthlyCond cond = MonthlyCond.builder().teamMember(admin1).year(year).month(month).build();
            private List<QueryMonthlySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.findMonthlyOrders(cond);
            }

            @Test
            @DisplayName("팀의 해당 월의 모든 주문을 가져와야 한다.")
            void it_returns_all_monthly_orders_for_admin() {
                assertThat(schedules.size()).isEqualTo(20);
            }
        }

        @Nested
        @DisplayName("주문자일 경우")
        class Context_orderer {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final MonthlyCond cond = MonthlyCond.builder().teamMember(orderer1).year(year).month(month).build();
            private List<QueryMonthlySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.findMonthlyOrders(cond);
            }

            @Test
            @DisplayName("팀의 해당 월의 자신이 주문한 주문을 모두 가져와야 한다.")
            void it_returns_all_monthly_orders_for_orderer() {
                assertThat(schedules.size()).isEqualTo(10);
            }
        }
    }

    @Nested
    @DisplayName("findMonthlyInforms 메소드는")
    class findMonthlyInformsTests {

        @Nested
        @DisplayName("사용자와 연관된 공지일 경우")
        class Context_relatedUser {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final MonthlyCond cond = MonthlyCond.builder().teamMember(viewer1).year(year).month(month).build();
            private List<QueryMonthlySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.findMonthlyInforms(cond);
            }

            @Test
            @DisplayName("월의 공지 및 일정을 모두 가져와야 한다.")
            void it_returns_all_monthly_informs_for_user() {
                assertThat(schedules.size()).isEqualTo(10);
            }
        }
    }

    @Nested
    @DisplayName("findDailyInforms 메소드는")
    class findDailyInformsTests {

        @Nested
        @DisplayName("특정 날짜가 주어진 경우")
        class Context_specificDate {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final int day = today.getDayOfMonth();
            final DailyCond cond = DailyCond.builder().year(year).month(month).day(day).teamMember(viewer1).build();
            private List<QuerySchedule> schedules;

            @BeforeEach
            void setUpContext() {
                schedules = scheduleRepository.findDailyInforms(cond);
            }

            @Test
            @DisplayName("공지 및 일정목록을 반환해야 한다.")
            void it_returns_daily_informs() {
                assertThat(schedules.size()).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("findDailyOrders 메소드는")
    class findDailyOrdersTests {

        @Nested
        @DisplayName("특정 날짜가 주어진 경우")
        class Context_specificDate {
            final int year = today.getYear();
            final int month = today.getMonthValue();
            final int day = today.getDayOfMonth();
            final DailyCond cond = DailyCond.builder().year(year).month(month).day(day).teamMember(orderer1).build();
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


    private void initData() throws Exception {
        // 팀 생성
        Team team = createTeam();
        em.persist(team);

        // 사용자 4명 생성
        Member adm1 = createMember();
        em.persist(adm1);
        Member ord1 = createMember();
        em.persist(ord1);
        Member ord2 = createMember();
        em.persist(ord2);
        Member view1 = createMember();
        em.persist(view1);

        // 팀에 사용자 배정
        em.persist(createParticipant(team, adm1, MemberTeamRole.ADMIN));
        em.persist(createParticipant(team, ord1, MemberTeamRole.ORDERER));
        em.persist(createParticipant(team, ord2, MemberTeamRole.ORDERER));
        em.persist(createParticipant(team, view1, MemberTeamRole.VIEWER));

        // 재고 현황 생성
        Inventory inventory = createInventory(team);
        em.persist(inventory);

        // 상품 생성
        Product product = createProduct(inventory);
        em.persist(product);


        // 주문자1이 오늘부터 2일 간격으로 총 10개 주문을 생성
        for (int i = 0; i < 10; i++) {
            LocalDate date = baseTime.plusDays(i * 2L).toLocalDate();
            Order order = createOrder(team, ord1, product, date);
            em.persist(order);
        }

        // 주문자2가 내일부터 2일 간격으로 총 10개의 주문을 생성
        for (int i = 0; i < 10; i++) {
            LocalDate date = baseTime.plusDays(i * 2L + 1).toLocalDate();
            Order order = createOrder(team, ord2, product, date);
            em.persist(order);
            em.persist(createOrderMention(order, adm1));
        }

        // 뷰어가 오늘 부터 1일 간격으로 총10 개의 공지를 작성하고 각각에 대해 관리자만 태그
        for (int i = 0; i < 10; i++) {
            LocalDate date = baseTime.plusDays(i).toLocalDate();
            Inform inform = createInform(team, view1, date);
            em.persist(inform);
            em.persist(createInformMention(inform, adm1));
        }
    }
}



