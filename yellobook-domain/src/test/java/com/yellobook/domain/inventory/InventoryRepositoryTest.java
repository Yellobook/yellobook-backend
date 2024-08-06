package com.yellobook.domain.inventory;

import com.yellobook.config.TestConfig;
import com.yellobook.domains.inventory.dto.InventoryDTO;
import com.yellobook.domains.inventory.dto.ProductDTO;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@DisplayName("Inventory 도메인 Repository Unit Test")
public class InventoryRepositoryTest {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final TeamRepository teamRepository;
    private Team team;

    @Autowired
    public InventoryRepositoryTest(InventoryRepository inventoryRepository, ProductRepository productRepository, TeamRepository teamRepository){
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.teamRepository = teamRepository;
    }

    @BeforeEach
    void setUpTeam(){
        team = Team.builder()
                .name("team1")
                .phoneNumber("000-0000-0000")
                .address("주소1")
                .build();
        teamRepository.save(team);
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
            Inventory inventory1 = Inventory.builder()
                    .team(team)
                    .title("2024년 08월 06일 재고현황")
                    .view(10)
                    .build();
            Inventory inventory2 = Inventory.builder()
                    .team(team)
                    .title("2024년 08월 07일 재고현황")
                    .view(5)
                    .build();

            inventoryRepository.save(inventory1);
            inventoryRepository.save(inventory2);

            //when
            List<InventoryDTO> result = inventoryRepository.getTotalInventory(teamId, pageable);

            //then
            assertThat(result.size()).isEqualTo(2);
            assertThat(result.get(0).getCreatedAt()).isAfterOrEqualTo(result.get(1).getCreatedAt());
        }

        @Test
        @DisplayName("팀에 재고가 존재하지 않으면 빈 리스트를 반환한다.")
        void getEmptyInventories(){
            //given
            Long teamId = team.getId();
            Pageable pageable = PageRequest.of(0, 5);

            //when
            List<InventoryDTO> result = inventoryRepository.getTotalInventory(teamId, pageable);

            //then
            assertThat(result.size()).isEqualTo(0);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("페이징이 잘 동작하는지 확인한다.")
        void getInventoriesPagingApplied(){
            //given
            Long teamId = team.getId();
            Pageable pageable = PageRequest.of(1, 1);
            Inventory inventory1 = Inventory.builder()
                    .team(team)
                    .title("2024년 08월 06일 재고현황")
                    .view(10)
                    .build();
            Inventory inventory2 = Inventory.builder()
                    .team(team)
                    .title("2024년 08월 07일 재고현황")
                    .view(5)
                    .build();

            inventoryRepository.save(inventory1);
            inventoryRepository.save(inventory2);

            //when
            List<InventoryDTO> result = inventoryRepository.getTotalInventory(teamId, pageable);

            //then
            assertThat(result.size()).isEqualTo(1);
        }

    }

    @Nested
    @DisplayName("제품")
    class productsTests{
        private Inventory inventory;
        private Product product1;

        @BeforeEach
        void setUpInventory(){
            inventory = Inventory.builder()
                    .team(team)
                    .title("2024년 08월 06일 재고현황")
                    .view(10)
                    .build();
            inventoryRepository.save(inventory);

            product1 = Product.builder()
                    .name("product1")
                    .subProduct("sub1")
                    .sku(1)
                    .purchasePrice(1000)
                    .salePrice(2000)
                    .amount(100)
                    .build();
            Product product2 = Product.builder()
                    .name("product2")
                    .subProduct("sub2")
                    .sku(2)
                    .purchasePrice(2000)
                    .salePrice(2000)
                    .amount(200)
                    .build();
            product1.modifyInventory(inventory);
            product2.modifyInventory(inventory);
            productRepository.save(product1);
            productRepository.save(product2);
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
                List<ProductDTO> result = productRepository.getProducts(inventoryId);

                //then
                assertThat(result.size()).isEqualTo(2);
                assertThat(result.get(0).getName()).isEqualTo("product1");
                assertThat(result.get(0).getName()).isLessThanOrEqualTo(result.get(1).getName());
            }

            @DisplayName("키워드를 포함한 제품을 조회하는지 테스트")
            @ParameterizedTest
            @CsvSource(value = {
                    "product, 2",
                    "1, 1"
            })
            void getProductsByKeyword(String keyword, int total){
                //given
                Long inventoryId = inventory.getId();

                //when
                List<ProductDTO> result = productRepository.getProducts(inventoryId, keyword);

                //then
                assertThat(result.size()).isEqualTo(total);
            }
        }

        @Test
        @DisplayName("제품 id로 제품 삭제되었는지 테스트")
        void deleteProduct(){
            //given
            Long productId = product1.getId();

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
            Integer sku = product1.getSku();

            //when
            boolean exist1 = productRepository.existsByInventoryIdAndSku(inventoryId, sku);

            //then
            assertThat(exist1).isTrue();
        }

    }

}
