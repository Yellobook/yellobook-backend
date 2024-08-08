package com.yellobook.domains.schedule.repository;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.config.ScheduleRepositoryTestConfig;
import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderMention;
import com.yellobook.domains.schedule.dto.EarliestCond;
import com.yellobook.domains.schedule.dto.SearchMonthlyCond;
import com.yellobook.domains.schedule.dto.query.QuerySchedule;
import com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.ScheduleType;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ScheduleRepositoryTestConfig.class)
@DisplayName("ScheduleRepository 테스트")
public class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @PersistenceContext
    EntityManager em;

    // 오늘 날짜
    private final LocalDate today = LocalDate.of(2024,  8, 3);

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
     *
     * 주문자1은 8.3, 8.5, 8.6 ... 총 주문 10개 (2일 간격)
     * 주문자2는 8.4, 8.6 ... 총 주문 10개 (2일 간격)
     * 뷰어1은 8.3, 8.4, 8.5 ... 총 공지 및 일정 10개 (1일 간격)
     * 관리자가 태그된 공지 및 일정 총 10개 (뷰어1이 작성한 모든 공지 및 일정에 태그)
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
    @DisplayName("findEarliestOrder 는")
    class findEarliestOrderTests {

        @Test
        @DisplayName("주문자라면 팀에서 자신이 주문한 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
        void testFindEarliestOrderForOrderer() {
            // given
            var cond = EarliestCond.builder()
                    .teamMember(orderer1)
                    .today(today)
                    .build();

            // when
            Optional<QueryUpcomingSchedule> result = scheduleRepository.findEarliestOrder(cond);

            // then
            assertThat(result).isPresent();
            QueryUpcomingSchedule schedule = result.get();
            assertThat(schedule.title()).isEqualTo("제품1 서브제품1 1개");
            assertThat(schedule.day()).isEqualTo("2024-08-05");
            assertThat(schedule.scheduleType()).isEqualTo(ScheduleType.ORDER);
        }

        @Test
        @DisplayName("관리자라면 팀에서 모든 주문 중 내일부터 가장 이른 주문을 가져와야 한다.")
        void testFindEarliestOrderForAdmin() {
            // given
            var cond = EarliestCond.builder()
                    .teamMember(admin1)
                    .today(today)
                    .build();

            // when
            Optional<QueryUpcomingSchedule> result = scheduleRepository.findEarliestOrder(cond);

            // then
            assertThat(result).isPresent();
            QueryUpcomingSchedule schedule = result.get();
            assertThat(schedule.title()).isEqualTo("제품1 서브제품1 1개");
            assertThat(schedule.day()).isEqualTo("2024-08-04");
            assertThat(schedule.scheduleType()).isEqualTo(ScheduleType.ORDER);
        }
    }

    @Nested
    @DisplayName("findEarliestInform 는")
    class findEarliestInformTests {

        @Test
        @DisplayName("사용자와 관련된 공지 및 일정 중 내일부터 가장 이른 공지 및 일정 을 가져와야 한다.")
        void testFindEarliestInformForMember() {
            // given
            var cond = EarliestCond.builder()
                    .today(today)
                    .teamMember(viewer1)
                    .build();

            // when
            Optional<QueryUpcomingSchedule> result = scheduleRepository.findEarliestInform(cond);

            // then
            assertThat(result).isPresent();
            QueryUpcomingSchedule schedule = result.get();

            assertThat(schedule.title()).isEqualTo("공지 및 일정");
            assertThat(schedule.day()).isEqualTo("2024-08-04");;
            assertThat(schedule.scheduleType()).isEqualTo(ScheduleType.INFORM);
        }
    }
    

    private void initData() throws Exception{
        // 팀 생성
        Team team = createTeam();
        em.persist(team);

        // 사용자 3명 생성
        Member adm1 = createMember(admin1);
        em.persist(adm1);
        Member ord1 = createMember(orderer1);
        em.persist(ord1);
        Member ord2 = createMember(orderer2);
        em.persist(ord2);
        Member view1 = createMember(viewer1);
        em.persist(view1);

        // 팀에 사용자 배정
        participate(team, adm1, MemberTeamRole.ADMIN);
        participate(team, ord1, MemberTeamRole.ORDERER);
        participate(team, ord2, MemberTeamRole.ORDERER);
        participate(team, view1, MemberTeamRole.VIEWER);

        // 재고 현황 생성
        Inventory inventory = createInventory(team);
        em.persist(inventory);

        // 상품 생성
        Product product = createProduct(inventory);
        em.persist(product);


        // 주문자1이 오늘부터 2일 간격으로 총 10개 주문을 생성
        for (int i = 0; i < 10; i++) {
            LocalDate date = baseTime.plusDays(i* 2L).toLocalDate();
            Order order = createOrder(team, ord1, product, date);
            em.persist(order);
        }

        // 주문자2가 내일부터 2일 간격으로 총 10개의 주문을 생성
        for (int i = 0; i < 10; i++) {
            LocalDate date = baseTime.plusDays(i* 2L+ 1).toLocalDate();
            Order order = createOrder(team, ord2, product, date);
            em.persist(order);
        }

        // 뷰어가 오늘 부터 1일 간격으로 총10 개를 공지및 일정을 작성하고 각각에 대해 관리자만 태그
        for (int i = 0; i< 10; i++) {
            LocalDate date = baseTime.plusDays(i).toLocalDate();
            Inform inform = createInform(team, view1, date);
            em.persist(inform);
            InformMention informMention = createInformMention(inform, adm1);
            em.persist(informMention);
        }
    }

    private Order createOrder(Team team, Member member, Product product, LocalDate orderDate) throws Exception {
        Order order = Order.builder()
                .view(1)
                .memo("메모")
                .date(orderDate)
                .orderStatus(OrderStatus.CONFIRMED)
                .orderAmount(1)
                .team(team)
                .member(member)
                .product(product)
                .build();
        setBaseTimeEntityFields(order, baseTime);
        return order;
    }

    private OrderMention createOrderMention(Order order, Member member) throws Exception {
         return OrderMention.builder()
                .order(order)
                .member(member)
                .build();
    }

    private InformMention createInformMention(Inform inform, Member member) throws Exception {
        return InformMention.builder()
                .inform(inform)
                .member(member)
                .build();
    }

    private Inform createInform(Team team, Member member, LocalDate date) throws  Exception{
        Inform inform = Inform.builder()
                .title("공지 및 일정")
                .content("내용")
                .view(0)
                .date(date)
                .team(team)
                .member(member)
                .build();
        setBaseTimeEntityFields(inform, baseTime);
        return inform;
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

    private Member createMember(TeamMemberVO teamMember) throws Exception {
        Member member = Member.builder()
                .nickname(String.format("사용자%d", teamMember.getMemberId()))
                .email(String.format("user%d@example.com", teamMember.getMemberId()))
                .profileImage(String.format("profile%d.png", teamMember.getMemberId()))
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
     * createNativeQuery 로 JPQL 이 제공하지 않는  데이터베이스의 특정 기능을 사용할 수 있다.
     */

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



