package com.yellobook.domain.schedule;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.config.TestConfig;
import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.schedule.dto.EarliestCond;
import com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule;
import com.yellobook.domains.schedule.repository.ScheduleRepository;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.ScheduleType;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = TestConfig.class)
@DataJpaTest
@DisplayName("PostRepository 테스트")
public class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    JdbcTemplate jdbcTemplate;

    // 오늘 날짜
    private final LocalDate today = LocalDate.of(2024,  8, 3);
    // 서비스 사용자
    private final TeamMemberVO admin = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
    private final TeamMemberVO orderer = TeamMemberVO.of(2L, 1L, MemberTeamRole.ORDERER);
    private final TeamMemberVO viewer = TeamMemberVO.of(3L, 1L, MemberTeamRole.VIEWER);

    private final Long INVALID_VALUE = 999999L;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime baseTime = LocalDateTime.parse("2024-08-03 08:00:00", formatter);

    @BeforeEach
    void setUp() throws Exception {
        //
        resetAutoIncrement();

        // 팀 생성
        Team team = createTeam();
        em.persist(team);

        // 사용자 3명 생성
        Member member1 = createMember(1);
        em.persist(member1);
        Member member2 = createMember(2);
        em.persist(member2);
        Member member3 = createMember(3);
        em.persist(member3);

        // 팀에 사용자 배정
        participate(team, member1, MemberTeamRole.ADMIN);
        participate(team, member2, MemberTeamRole.ORDERER);
        participate(team, member3, MemberTeamRole.VIEWER);

        // 재고 현황 생성
        Inventory inventory = createInventory(team);
        em.persist(inventory);

        // 상품 생성
        Product product = createProduct(inventory);
        em.persist(product);
        List<Member> members = List.of(member1, member2, member3);

        // 주문 10개씩 생성
        for (int i = 0; i < 2; i++) {
            Member member = members.get(i);
            for (int j = 0; j < 10; j++) {
                createOrder(team, member, product, j);
            }
        }

        // 공지 및 일정 10개씩 생성
        for (int i = 0; i < 3; i++) {
            Member member = members.get(i);
            for (int j = 0; j < 10; j++) {
                createInform(team, member, j);
            }
        }
    }
    private void resetAutoIncrement() {
        jdbcTemplate.execute("ALTER TABLE teams ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE participants ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE inventories ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE products ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE orders ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE informs ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    public void testDatabaseInitialization() {
        Long memberCount = em.createQuery("SELECT COUNT(m) FROM Member m", Long.class).getSingleResult();
        Long teamCount = em.createQuery("SELECT COUNT(t) FROM Team t", Long.class).getSingleResult();
        Long participantCount = em.createQuery("SELECT COUNT(p) FROM Participant p", Long.class).getSingleResult();
        Long inventoryCount = em.createQuery("SELECT COUNT(i) FROM Inventory i", Long.class).getSingleResult();
        Long productCount = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();
        Long orderCount = em.createQuery("SELECT COUNT(o) FROM Order o", Long.class).getSingleResult();
        Long informCount = em.createQuery("SELECT COUNT(i) FROM Inform i", Long.class).getSingleResult();

        assertEquals(3, memberCount);
        assertEquals(1, teamCount);
        assertEquals(3, participantCount);
        assertEquals(1, inventoryCount);
        assertEquals(1, productCount);
        assertEquals(20, orderCount);
        assertEquals(30, informCount);
    }

    @Nested
    @DisplayName("findEarliestOrder 는")
    class FindEarliestOrderTests {

        @Test
        @DisplayName("주문자라면 팀에서 자신이 주문한 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
        void testFindEarliestOrderForOrderer() {
            // given
            var cond = EarliestCond.builder()
                    .teamMember(orderer)
                    .today(today)
                    .build();

            // when
            Optional<QueryUpcomingSchedule> result = scheduleRepository.findEarliestOrder(cond);

            // then
            assertThat(result).isPresent();
            QueryUpcomingSchedule schedule = result.get();
            assertThat(schedule.getTitle()).isEqualTo("제품1 서브제품1 1개");
            assertThat(schedule.getDay()).isEqualTo("2024-08-04");
            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.ORDER);
        }

        @Test
        @DisplayName("관리자라면 팀에서 모든 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
        void testFindEarliestOrderForAdmin() {
            // given
            var cond = EarliestCond.builder()
                    .teamMember(admin)
                    .today(today)
                    .build();

            // when
            Optional<QueryUpcomingSchedule> result = scheduleRepository.findEarliestOrder(cond);

            // then
            assertThat(result).isPresent();
            QueryUpcomingSchedule schedule = result.get();
            assertThat(schedule.getTitle()).isEqualTo("제품1 서브제품1 1개");
            assertThat(schedule.getDay()).isEqualTo("2024-08-04");
            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.ORDER);
        }

        @Test
        @DisplayName("없는 사용자라면 결과가 비어 있어야 한다.")
        void testFindEarliestOrderWithInvalidMemberId() {
            // given
            TeamMemberVO notExistMember = TeamMemberVO.of(INVALID_VALUE, 1L, MemberTeamRole.ORDERER);

            var cond = EarliestCond.builder()
                    .teamMember(notExistMember)
                    .today(today)
                    .build();

            // when
            Optional<QueryUpcomingSchedule> result = scheduleRepository.findEarliestOrder(cond);

            // then
            assertThat(result).isEmpty();
        }
    }
//
//    @Nested
//    @DisplayName("findEarliestInform 는")
//    class findEarliestInformTests {
//
//        @Test
//        @DisplayName("사용자와 관련된 가장 이른 공지 및 일정 을 가져와야 한다.")
//        void testFindEarliestInformForMember() {
//            // given
//            TeamMemberVO teamMember = admin;
//
//            // when
//            Optional<QueryUpcomingSchedule> result = scheduleQueryRepository.findEarliestInform(today, teamMember);
//
//            // then
//            assertThat(result).isPresent();
//            QueryUpcomingSchedule schedule = result.get();
//
//            assertThat(schedule.getTitle()).isEqualTo("공지 및 일정1");
//            assertThat(schedule.getDay()).isEqualTo(LocalDate.of(2024, 7, 26));
//            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.INFORM);
//        }
//
//        @Test
//        @DisplayName("팀 구성원이 아닌 경우 결과가 없어야 한다.")
//        void testFindEarliestInformForNonTeamMember() {
//            // given
//            Long invalidTeamMemberId = INVALID_VALUE;
//            TeamMemberVO teamMember = TeamMemberVO.of(invalidTeamMemberId, 1L, MemberTeamRole.ADMIN);
//
//            // when
//            Optional<QueryUpcomingSchedule> result = scheduleQueryRepository.findEarliestInform(today, teamMember);
//
//            // then
//            assertThat(result).isNotPresent();
//        }
//
//        @Test
//        @DisplayName("태그된 일정이 존재하면 해당 일정도 가져와야 한다.")
//        void testFindEarliestInformForMentionedMember() {
//            // given
//            TeamMemberVO teamMember = orderer;
//
//            // when
//            Optional<QueryUpcomingSchedule> result = scheduleQueryRepository.findEarliestInform(today, teamMember);
//
//            // then
//            assertThat(result).isPresent();
//            QueryUpcomingSchedule schedule = result.get();
//            assertThat(schedule.getTitle()).isEqualTo("공지 및 일정1");
//            assertThat(schedule.getDay()).isEqualTo(LocalDate.of(2024, 7, 26));
//            assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.INFORM);
//        }
//    }
//
//    @Nested
//    @DisplayName("searchMonthlyOrders 는")
//    class searchMonthlyOrdersTests {
//
//        @Test
//        @DisplayName("특정 키워드를 포함한 특정 월의 주문 목록을 가져와야 한다.")
//        void testSearchMonthlyOrdersForMember() {
//            // given
//            String keyword = "제품";
//            TeamMemberVO teamMember = admin;
//            int year = 2024;
//            int month = 7;
//
//            // when
//            List<QuerySchedule> result = scheduleQueryRepository.searchMonthlyOrders(keyword, year, month, teamMember);
//
//            // then
//            assertThat(result.size()).isGreaterThan(0);
//            assertThat(result).isNotEmpty();
//            result.forEach(schedule -> {
//                assertThat(schedule.getTitle()).contains(keyword);
//                assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.ORDER);
//                assertThat(schedule.getDate().getYear()).isEqualTo(year);
//                assertThat(schedule.getDate().getMonthValue()).isEqualTo(month);
//            });
//
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 키워드로 검색 시 빈 리스트를 반환해야 한다.")
//        void testSearchMonthlyOrdersWithNonExistentKeyword() {
//            // given
//            String keyword = "없는 키워드";
//            TeamMemberVO teamMember = admin;
//            int year = 2024;
//            int month = 7;
//
//            // when
//            List<QuerySchedule> result = scheduleQueryRepository.searchMonthlyOrders(keyword, year, month, teamMember);
//
//            // then
//            assertThat(result).isEmpty();
//        }
//
//        @Test
//        @DisplayName("특정 키워드와 연관된 주문 목록을 올바르게 정렬해야 한다.")
//        void testSearchMonthlyOrdersSortedOrder() {
//            // given
//            String keyword = "제품";
//            TeamMemberVO teamMember = admin;
//            int year = 2024;
//            int month = 7;
//
//            // when
//            List<QuerySchedule> result = scheduleQueryRepository.searchMonthlyOrders(keyword, year, month, teamMember);
//
//            // then
//            assertThat(result).isNotEmpty();
//            assertThat(result).isSortedAccordingTo(Comparator.comparing(QuerySchedule::getDate));
//        }
//    }
//
//    @Nested
//    @DisplayName("searchMonthlyInforms는")
//    class searchMonthlyInformsTests {
//
//        @Test
//        @DisplayName("자신이 작성한 일정을 검색할 수 있어야 한다.")
//        void testSearchMonthlyInformsForOwn() {
//            // given
//            String keyword = "일정";
//            TeamMemberVO teamMember = admin;
//            int year = 2024;
//            int month = 7;
//
//            // when
//            List<QuerySchedule> schedules = scheduleQueryRepository.searchMonthlyInforms(keyword, year, month, teamMember);
//
//            // then
//            assertThat(schedules).isNotEmpty();
//            assertThat(schedules).extracting("title").contains("공지 및 일정7");
//            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
//        }
//
//        @Test
//        @DisplayName("자신이 태그된 일정을 검색할 수 있다.")
//        void testSearchMonthlyInformsForTagged() {
//            // given
//            String keyword = "일정";
//            TeamMemberVO teamMember = admin;
//            int year = 2024;
//            int month = 7;
//
//            // when
//            List<QuerySchedule> schedules = scheduleQueryRepository.searchMonthlyInforms(keyword, year, month, teamMember);
//
//            // then
//            assertThat(schedules).isNotEmpty();
//            assertThat(schedules).extracting("title").contains("공지 및 일정3");
//            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
//        }
//
//        @Test
//        @DisplayName("키워드에 해당하는 글이 없을 경우 빈 리스트를 반환한다.")
//        void testSearchMonthlyInformsWithNoKeyword() {
//            // given
//            String keyword = "[][[][][][][]";
//            TeamMemberVO teamMember = admin;
//            int year = 2024;
//            int month = 8;
//
//            // when
//            List<QuerySchedule> schedules = scheduleQueryRepository.searchMonthlyInforms(keyword, year, month, teamMember);
//
//            // then
//            assertThat(schedules).isEmpty();
//        }
//
//        @Test
//        @DisplayName("해당 월에 속하지 않는 일정을 검색하지 않는다.")
//        void testSearchMonthlyInformsWithDifferentMonth() {
//            // given
//            String keyword = "내용";
//            TeamMemberVO teamMember = admin;
//            int year = 2024;
//            int month = 6;
//
//            // when
//            List<QuerySchedule> schedules = scheduleQueryRepository.searchMonthlyInforms(keyword, year, month, teamMember);
//
//            // then
//            assertThat(schedules).isEmpty();
//        }
//    }
//
//
//    @Nested
//    @DisplayName("findMonthlyOrders 는")
//    class FindMonthlyOrdersTests {
//
//        @Test
//        @DisplayName("관리자일 경우 팀의 해당 월의 주문을 모두 가져와야 한다.")
//        void testFindMonthlyOrdersAdmin() {
//            // given
//            TeamMemberVO teamMember = admin;
//            int year = 2024;
//            int month = 7;
//            MemberTeamRole role = MemberTeamRole.ADMIN;
//
//            // when
//            List<QueryMonthlySchedule> schedules = scheduleQueryRepository.findMonthlyOrders(year, month, teamMember);
//
//            // then
//            assertThat(schedules).isNotEmpty();
//            assertThat(schedules).extracting("title").contains(
//                    "제품 A 서브제품1 10개",
//                    "제품 A 서브제품1 5개",
//                    "제품 B 서브제품2 20개",
//                    "제품 A 서브제품1 12개",
//                    "제품 B 서브제품2 8개",
//                    "제품 A 서브제품1 18개");
//            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
//        }
//
//        @Test
//        @DisplayName("주문자일 경우 팀의 해당 월의 주문을 모두 가져와야 한다.")
//        void testFindMonthlyOrdersOrderer() {
//            // given
//            TeamMemberVO teamMember = orderer;
//            int year = 2024;
//            int month = 7;
//            MemberTeamRole role = MemberTeamRole.ORDERER;
//
//            // when
//            List<QueryMonthlySchedule> schedules = scheduleQueryRepository.findMonthlyOrders(year, month, teamMember);
//
//            // then
//            assertThat(schedules).isNotEmpty();
//            assertThat(schedules).extracting("title").contains(
//                    "제품 A 서브제품1 5개",
//                    "제품 A 서브제품1 12개"
//            );
//
//            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
//        }
//    }
//
//    @Nested
//    @DisplayName("findMonthlyInforms 는")
//    class FindMonthlyInformsTests {
//
//        @Test
//        @DisplayName("사용자와 연관된 월의 공지 및 일정을 모두 가져와야 한다.")
//        void testFindMonthlyOrdersAdmin() {
//            // given
//            TeamMemberVO teamMember = admin;
//            int year = 2024;
//            int month = 7;
//            MemberTeamRole role = MemberTeamRole.ADMIN;
//
//            // when
//            List<QueryMonthlySchedule> schedules = scheduleQueryRepository.findMonthlyOrders(year, month, teamMember);
//
//            // then
//            assertThat(schedules).isNotEmpty();
//            assertThat(schedules).extracting("title").contains(
//                    "제품 A 서브제품1 10개",
//                    "제품 A 서브제품1 5개",
//                    "제품 B 서브제품2 20개",
//                    "제품 A 서브제품1 12개",
//                    "제품 B 서브제품2 8개",
//                    "제품 A 서브제품1 18개");
//            assertThat(schedules).extracting("date").allMatch(date -> ((LocalDate) date).getYear() == year && ((LocalDate) date).getMonthValue() == month);
//        }
//    }
//
//
//    @Nested
//    @DisplayName("findDailyInforms는")
//    class FindDailyInformsTests {
//
//        @Test
//        @DisplayName("특정 날짜의 일정을 가져와야 한다.")
//        void testFindDailyInforms() {
//            // given
//            int year = 2024;
//            int month = 7;
//            int day = 26;
//            TeamMemberVO teamMember = admin;
//
//            // when
//            List<QuerySchedule> schedules = scheduleQueryRepository.findDailyInforms(year, month, day, teamMember);
//
//            // then
//            assertThat(schedules).isNotEmpty();
//            assertThat(schedules)
//                    .extracting("title")
//                    .contains("공지 및 일정1"); // Example title from the given data
//        }
//    }
//
//    @Nested
//    @DisplayName("findDailyOrders는")
//    class FindDailyOrdersTests {
//
//        @Test
//        @DisplayName("특정 날짜의 주문을 가져와야 한다.")
//        void testFindDailyOrders() {
//            // given
//            int year = 2024;
//            int month = 7;
//            int day = 26;
//            TeamMemberVO teamMember = admin;
//
//            // when
//            List<QuerySchedule> schedules = scheduleQueryRepository.findDailyOrders(year, month, day, teamMember);
//
//            // then
//            assertThat(schedules).isNotEmpty();
//        }
//    }

    private void createOrder(Team team, Member member, Product product, int i) throws Exception {
        Order order = Order.builder()
                .view(1)
                .memo("메모")
                .date(baseTime.plusDays(i).toLocalDate())
                .orderStatus(OrderStatus.CONFIRMED)
                .orderAmount(1)
                .team(team)
                .member(member)
                .product(product)
                .build();
        setBaseTimeEntityFields(order, baseTime.plusDays(i));
        System.out.println("baseTime.plusDays(i) = " + baseTime.plusDays(i));
        em.persist(order);
        System.out.println("order.getTeam().getId() = " + order.getTeam().getId());
    }

    private void createInform(Team team, Member member, int i) throws  Exception{
        Inform inform = Inform.builder()
                .title("공지 및 일정" + i)
                .content("내용" + i)
                .view(0)
                .date(baseTime.plusDays(i).toLocalDate())
                .team(team)
                .member(member)
                .build();
        setBaseTimeEntityFields(inform, baseTime.plusDays(i));
        em.persist(inform);
    }

    private Product createProduct(Inventory inventory) {
        return Product.builder()
                .name("제품1")
                .subProduct("서브제품1")
                .sku(1)
                .purchasePrice(100)
                .salePrice(150)
                .amount(100)
                .inventory(inventory)
                .build();
    }


    private Inventory createInventory(Team team) {
        return Inventory.builder()
                .team(team)
                .title("2024년 08월 06일 재고현황")
                .view(1000)
                .build();
    }

    private void participate(Team team, Member member, MemberTeamRole role) {
        Participant participant = Participant.builder()
                .team(team)
                .member(member)
                .role(role)
                .build();
        em.persist(participant);
    }

    private Member createMember(int i) throws Exception {
        Member member = Member.builder()
                .nickname(String.format("사용자%d", i))
                .email(String.format("user%d@example.com", i))
                .profileImage(String.format("profile%d.png", i))
                .role(MemberRole.USER)
                .allowance(true)
                .build();
        setBaseTimeEntityFields(member, baseTime);
        return member;
    }

    private Team createTeam() throws Exception {
        Team team = Team.builder()
                .name("팀1")
                .phoneNumber("010-1234-5678")
                .address("서울특별시")
                .build();
        setBaseTimeEntityFields(team, baseTime);
        return team;
    }

    /**
     * BaseEntity 의 createdAt과 updatedAt 필드는 JPA Auditing을 통해 자동으로 설정된다.
     * 테스트에서는 특정 시간 값을 설정해야 하므로 리플렉션을 사용하여 필드를 수동으로 설정한다.
     */
    private void setBaseTimeEntityFields(Object entity, LocalDateTime timestamp) throws Exception {
        Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
        Field updatedAtField = BaseEntity.class.getDeclaredField("updatedAt");
        createdAtField.setAccessible(true);
        updatedAtField.setAccessible(true);
        createdAtField.set(entity, timestamp);
        updatedAtField.set(entity, timestamp);
    }

}



