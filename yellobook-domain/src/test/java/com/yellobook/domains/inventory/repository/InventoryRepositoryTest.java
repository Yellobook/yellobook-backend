package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.dto.query.QueryInventory;
import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.dto.query.QueryProductName;
import com.yellobook.domains.inventory.dto.query.QuerySubProduct;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.team.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@EnableJpaAuditing
@DisplayName("Inventory 도메인 Repository Unit Test")
public class InventoryRepositoryTest {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final TestEntityManager entityManager;
    private Team team;

    @Autowired
    public InventoryRepositoryTest(InventoryRepository inventoryRepository, ProductRepository productRepository,
                                   TestEntityManager entityManager){
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @BeforeEach
    void setUpTeam(){
        team = Team.builder()
                .name("team1")
                .phoneNumber("000-0000-0000")
                .address("주소1")
                .build();
        entityManager.persist(team);
    }

    @Nested
    @DisplayName("재고 전체 조회")
    class getTotalInventoryTests{

        @Test
        @DisplayName("팀의 재고 중 날짜를 기준으로 내림차순 정렬으로 가져온다.")
        void getInventoriesByDateDesc(){
            //given
            Long teamId = team.getId();
            Pageable pageable = PageRequest.of(0, 5);
            for(int i =0; i<6; i++){
                Inventory inventory = Inventory.builder()
                        .team(team)
                        .title(String.format("2024년 08월 0%d일 재고현황", i))
                        .build();
                entityManager.persist(inventory);
            }

            //when
            List<QueryInventory> result = inventoryRepository.getTotalInventory(teamId, pageable);

            //then
            assertThat(result.size()).isEqualTo(5);
            for(int i=1; i<result.size(); i++){
                assertThat(result.get(i-1).createdAt()).isAfterOrEqualTo(result.get(i).createdAt());
            }
        }

        @Nested
        @DisplayName("가장 최근 재고 조회")
        class GetRecentInventoryTests{
            @Test
            @DisplayName("가장 최근 재고 1개를 가지고 오는지 확인한다.")
            void getOneRecentInventory(){
                //given
                Long teamId = team.getId();
                for(int i =0; i<6; i++){
                    Inventory inventory = Inventory.builder()
                            .team(team)
                            .title(String.format("2024년 08월 0%d일 재고현황", i))
                            .build();
                    entityManager.persist(inventory);
                }

                //when
                Optional<Inventory> result = inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(teamId);

                //then
                assertThat(result).isNotEmpty();
                assertThat(result.get().getId()).isEqualTo(6L);
            }

            @Test
            @DisplayName("재고가 없으면 Optional.empty()를 반환한다.")
            void getEmptyInventory(){
                //given
                Long teamId = team.getId();

                //when
                Optional<Inventory> result = inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(teamId);

                //then
                assertThat(result).isEmpty();
            }
        }

        @Test
        @DisplayName("팀에 재고가 존재하지 않으면 빈 리스트를 반환한다.")
        void getEmptyInventories(){
            //given
            Long teamId = team.getId();
            Pageable pageable = PageRequest.of(0, 5);

            //when
            List<QueryInventory> result = inventoryRepository.getTotalInventory(teamId, pageable);

            //then
            assertThat(result.size()).isEqualTo(0);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("페이징이 잘 동작하는지 확인한다.")
        void getInventoriesPagingApplied(){
            //given
            Long teamId = team.getId();
            Pageable pageable = PageRequest.of(1, 5);
            for(int i =0; i<6; i++){
                Inventory inventory = Inventory.builder()
                        .team(team)
                        .title(String.format("2024년 08월 0%d일 재고현황", i))
                        .build();
                entityManager.persist(inventory);
            }

            //when
            List<QueryInventory> result = inventoryRepository.getTotalInventory(teamId, pageable);

            //then
            assertThat(result.size()).isEqualTo(1);
        }

    }

    @Nested
    @DisplayName("제품")
    class productsTests{
        private Inventory inventory;

        @BeforeEach
        void setUpInventory(){
            inventory = Inventory.builder()
                    .team(team)
                    .title("2024년 08월 06일 재고현황")
                    .build();
            entityManager.persist(inventory);

            for(int i =0; i<5; i++){
                Product product = Product.builder()
                        .name("product 제품")
                        .subProduct(String.format("sub%d", i))
                        .sku(i)
                        .purchasePrice(i*1000)
                        .salePrice(i*2000)
                        .amount(i+100)
                        .inventory(inventory)
                        .build();
                entityManager.persist(product);
            }
        }

        @Nested
        @DisplayName("제품 조회 테스트")
        class getProductsTests{
            @Test
            @DisplayName("제품명을 기준으로 오름차순 정렬이 되었는지 테스트")
            void getProductsByNameAsc(){
                //given
                Long inventoryId = inventory.getId();

                //when
                List<QueryProduct> result = productRepository.getProducts(inventoryId);

                //then
                assertThat(result.size()).isEqualTo(5);
                for(int i=1; i<result.size(); i++){
                    assertThat(result.get(i-1).name()).isLessThanOrEqualTo(result.get(i).name());
                }
            }

            @DisplayName("키워드를 포함한 제품을 조회하는지 테스트")
            @ParameterizedTest
            @CsvSource(value = {
                    "product, 5",
                    "p, 5",
                    "제품, 5",
                    "1, 0"
            })
            void getProductsByKeyword(String keyword, int total){
                //given
                Long inventoryId = inventory.getId();

                //when
                List<QueryProduct> result = productRepository.getProducts(inventoryId, keyword);

                //then
                assertThat(result.size()).isEqualTo(total);
            }

            @DisplayName("키워드를 포함하는 제품이 중복없이 조회되는지 확인")
            @ParameterizedTest
            @CsvSource(value = {
                    "product, 1",
                    "p, 1",
                    "제품, 1",
                    "1, 0"
            })
            void getProductNameDistinct(String keyword, int total){
                //given
                Long inventoryId = inventory.getId();

                //when
                List<QueryProductName> result = productRepository.getProductsName(inventoryId, keyword);

                //then
                assertThat(result.size()).isEqualTo(total);
            }


            @DisplayName("제품명과 일치하는 제품들을 하위 제품의 이름을 기준으로 내림차순 정렬하는지 테스트")
            @ParameterizedTest
            @CsvSource(value = {
                    "product 제품, 5",
                    "product, 0",
                    "1, 0",
                    "제품, 0"
            })
            void getSubProducts(String productName, int total){
                //given
                Long inventoryId = 1L;

                //when
                List<QuerySubProduct> result = productRepository.getSubProducts(inventoryId, productName);

                //then
                assertThat(result.size()).isEqualTo(total);
                for(int i=1; i<result.size(); i++){
                    assertThat(result.get(i-1).subProductName()).isLessThanOrEqualTo(result.get(i).subProductName());
                }
            }

        }

        @Test
        @DisplayName("제품 id로 제품 삭제되었는지 테스트")
        void deleteProduct(){
            //given
            Long productId = 1L;

            //when
            productRepository.deleteById(productId);

            //then
            assertThat(productRepository.existsById(productId)).isFalse();
        }

        @Test
        @DisplayName("inventory Id와 sku로 제품이 존재하는지 테스트")
        void getByInventoryIdAndSku(){
            //given
            Long inventoryId = inventory.getId();
            Integer sku = 1;

            //when
            boolean exist1 = productRepository.existsByInventoryIdAndSku(inventoryId, sku);

            //then
            assertThat(exist1).isTrue();
        }

    }

}
