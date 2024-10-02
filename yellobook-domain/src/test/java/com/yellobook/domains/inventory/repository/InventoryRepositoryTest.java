package com.yellobook.domains.inventory.repository;

import static fixture.InventoryFixture.createInventory;
import static fixture.InventoryFixture.createProduct;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.domains.inventory.dto.query.QueryInventory;
import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.support.annotation.RepositoryTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("InventoryRepository Unit Test")
public class InventoryRepositoryTest extends RepositoryTest {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager em;

    private Team team;

    @BeforeEach
    void setUp() {
        team = createTeam();
        em.persist(team);
    }

    @Nested
    @DisplayName("getTotalInventory 메소드는")
    class Describe_GetTotalInventory {
        Pageable pageable = PageRequest.of(0, 5);

        @Nested
        @DisplayName("재고가 존재하지 않으면")
        class Context_inventory_exist {

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                List<QueryInventory> result = inventoryRepository.getTotalInventory(team.getId(), pageable);

                assertThat(result.size()).isEqualTo(0);
                assertThat(result).isEmpty();
            }
        }

        @Nested
        @DisplayName("재고가 존재하면")
        class Context_inventory_not_exist {

            @BeforeEach
            void setUpContext() {
                for (int i = 0; i < 6; i++) {
                    Inventory inventory = createInventory(String.format("2024년 08월 0%d일 재고현황", i), team);
                    em.persist(inventory);
                }
            }

            @Test
            @DisplayName("재고를 날짜를 기준으로 내림차순으로 정렬해서 반환한다.")
            void it_returns_inventories_orderBy_createdAt_Desc() {
                List<QueryInventory> result = inventoryRepository.getTotalInventory(team.getId(), pageable);

                assertThat(result.size()).isEqualTo(5);
                for (int i = 1; i < result.size(); i++) {
                    assertThat(result.get(i - 1)
                            .createdAt()).isAfterOrEqualTo(result.get(i)
                            .createdAt());
                }
            }

            @Test
            @DisplayName("페이징을 반영해서 반환한다.")
            void it_returns_inventories_by_paging() {
                List<QueryInventory> result = inventoryRepository.getTotalInventory(team.getId(), pageable);

                assertThat(result.size()).isEqualTo(5);
            }

        }

    }

    @Nested
    @DisplayName("getProducts 메소드는")
    class Describe_GetProducts {
        private Inventory inventory;

        @BeforeEach
        void setUpContext() {
            inventory = createInventory("2024년 08월 06일 재고현황", team);
            em.persist(inventory);

            for (int i = 0; i < 5; i++) {
                Product product = createProduct(String.format("product%d", i), String.format("sub%d", i), i, i * 1000,
                        i * 2000, i * 100, inventory);
                em.persist(product);
            }
        }

        @Nested
        @DisplayName("재고 id가 제공되면")
        class Context_inventoy_id_given {

            @Test
            @DisplayName("제품명을 기준으로 오름차순으로 정렬해서 반환한다.")
            void it_returns_products_orderBy_name_asc() {
                List<QueryProduct> result = productRepository.getProducts(inventory.getId());

                assertThat(result.size()).isEqualTo(5);
                for (int i = 1; i < result.size(); i++) {
                    assertThat(result.get(i - 1)
                            .name()).isLessThanOrEqualTo(result.get(i)
                            .name());
                }
            }
        }

        @Nested
        @DisplayName("재고 id와 제품 이름 키워드가 제공되면")
        class Describe_Inventory_Id_And_Keyword_Given {
            @ParameterizedTest
            @CsvSource(value = {
                    "product, 5",
                    "1, 1",
                    "품, 0"
            })
            @DisplayName("키워드를 제품명에 포함하는 제품을 반환한다.")
            void it_returns_products_contain_keyword(String keyword, Integer total) {
                List<QueryProduct> result = productRepository.getProducts(inventory.getId(), keyword);

                assertThat(result.size()).isEqualTo(total);
            }
        }
    }

    @Nested
    @DisplayName("deleteById 메소드는")
    class Describe_DeleteById {

        @BeforeEach
        void setUpContext() {
            Inventory inventory = createInventory(team);
            em.persist(inventory);

            for (int i = 0; i < 5; i++) {
                Product product = createProduct(String.format("product%d", i), String.format("sub%d", i), i, i * 1000,
                        i * 2000, i * 100, inventory);
                em.persist(product);
            }
        }

        @Nested
        @DisplayName("제품 id가 제공되면")
        class Context_product_id_given {
            Long productId = 1L;

            @Test
            @DisplayName("해당 제춤을 삭제한다.")
            void it_delete_product() {
                productRepository.deleteById(productId);

                assertThat(productRepository.existsById(productId)).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("existsByInventoryIdAndSku 메소드는")
    class Describe_ExistsByInventoryIdAndSku {
        private Inventory inventory;

        @BeforeEach
        void setUpContext() {
            inventory = createInventory(team);
            em.persist(inventory);

            for (int i = 0; i < 5; i++) {
                Product product = createProduct(String.format("product%d", i), String.format("sub%d", i), i, i * 1000,
                        i * 2000, i * 100, inventory);
                em.persist(product);
            }
        }

        @Nested
        @DisplayName("재고 id와 sku가 제공되면")
        class Context_inventory_id_and_sku_given {
            Integer sku = 1;

            @Test
            @DisplayName("해당 재고에 포함되고, 해당 sku와 동일한 sku를 갖는 제품이 존재하는지 확인한다.")
            void it_exists_by_inventory_id_and_sku() {
                boolean exist1 = productRepository.existsByInventoryIdAndSku(inventory.getId(), sku);

                assertThat(exist1).isTrue();
            }
        }

    }

}
