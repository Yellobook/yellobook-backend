package com.yellobook.domains.inventory.mapper;

import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.domains.inventory.dto.query.QueryInventory;
import com.yellobook.domains.team.entity.Team;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("InventoryMapper Unit Test")
class InventoryMapperTest {
    InventoryMapper mapper = Mappers.getMapper(InventoryMapper.class);

    @Nested
    @DisplayName("toInventoryInfoList 메서드는")
    class Describe_toInventoryInfoList {
        @Nested
        @DisplayName("List<QueryInventory> 를 받아")
        class Context_with_queryInventoryList {
            List<QueryInventory> queryInventoryList;

            @BeforeEach
            void setUpContext() {
                queryInventoryList = new ArrayList<>(List.of(
                        QueryInventory.builder()
                                .inventoryId(1L)
                                .title("재고 1")
                                .createdAt(LocalDateTime.of(2024, 11, 1, 10, 0))
                                .updatedAt(LocalDateTime.of(2024, 11, 7, 12, 0))
                                .view(100)
                                .build(),
                        QueryInventory.builder()
                                .inventoryId(2L)
                                .title("재고 2")
                                .createdAt(LocalDateTime.of(2024, 11, 2, 11, 0))
                                .updatedAt(LocalDateTime.of(2024, 11, 7, 12, 5))
                                .view(150)
                                .build()
                ));
            }

            @Test
            @DisplayName("List<InventoryInfo> 를 반환한다.")
            void it_returns_InventoryInfo_List() {
                var target = mapper.toInventoryInfoList(queryInventoryList);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                IntStream.range(0, target.size())
                        .forEach(i -> {
                            var targetInventory = target.get(i);
                            var sourceInventory = queryInventoryList.get(i);
                            assertThat(targetInventory.inventoryId()).isEqualTo(sourceInventory.inventoryId());
                            assertThat(targetInventory.title()).isEqualTo(sourceInventory.title());
                            assertThat(LocalDateTime.parse(targetInventory.createdAt(), formatter)
                                    .truncatedTo(ChronoUnit.SECONDS)).isEqualTo(sourceInventory.createdAt()
                                    .truncatedTo(ChronoUnit.SECONDS));
                            assertThat(LocalDateTime.parse(targetInventory.updatedAt(), formatter)
                                    .truncatedTo(ChronoUnit.SECONDS)).isEqualTo(sourceInventory.updatedAt()
                                    .truncatedTo(ChronoUnit.SECONDS));
                            assertThat(targetInventory.view()).isEqualTo(sourceInventory.view());
                        });
            }
        }
    }

    @Nested
    @DisplayName("toGetTotalInventoryResponse 메서드는")
    class Describe_toGetTotalInventoryResponse {
        @Nested
        @DisplayName("Integer, List<QueryInventory> 를 받아")
        class Context_with_page_content {
            Integer page;
            List<QueryInventory> content;

            @BeforeEach
            void setUpContext() {
                page = 1;
                content = new ArrayList<>(List.of(
                        QueryInventory.builder()
                                .inventoryId(1L)
                                .title("재고 1")
                                .createdAt(LocalDateTime.of(2024, 11, 1, 10, 0))
                                .updatedAt(LocalDateTime.of(2024, 11, 7, 12, 0))
                                .view(100)
                                .build(),
                        QueryInventory.builder()
                                .inventoryId(2L)
                                .title("재고 2")
                                .createdAt(LocalDateTime.of(2024, 11, 2, 11, 0))
                                .updatedAt(LocalDateTime.of(2024, 11, 7, 12, 5))
                                .view(150)
                                .build()
                ));
            }

            @Test
            @DisplayName("GetTotalInventoryResponse 를 반환한다.")
            void it_returns_GetTotalInventoryResponse() {
                var target = mapper.toGetTotalInventoryResponse(page, content);
                assertThat(target.page()).isEqualTo(page);
                assertThat(target.size()).isEqualTo(content.size());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                IntStream.range(0, target.inventories()
                                .size())
                        .forEach(i -> {
                            var targetInventory = target.inventories()
                                    .get(i);
                            var sourceInventory = content.get(i);
                            assertThat(targetInventory.inventoryId()).isEqualTo(sourceInventory.inventoryId());
                            assertThat(targetInventory.title()).isEqualTo(sourceInventory.title());
                            assertThat(LocalDateTime.parse(targetInventory.createdAt(), formatter)
                                    .truncatedTo(ChronoUnit.SECONDS)).isEqualTo(sourceInventory.createdAt()
                                    .truncatedTo(ChronoUnit.SECONDS));
                            assertThat(LocalDateTime.parse(targetInventory.updatedAt(), formatter)
                                    .truncatedTo(ChronoUnit.SECONDS)).isEqualTo(sourceInventory.updatedAt()
                                    .truncatedTo(ChronoUnit.SECONDS));
                            assertThat(targetInventory.view()).isEqualTo(sourceInventory.view());
                        });
            }
        }
    }

    @Nested
    @DisplayName("toInventory 메서드는")
    class Describe_toInventory {
        @Nested
        @DisplayName("Team, String 을 받아")
        class Context_with_team_title {
            Team team;
            String title;

            @BeforeEach
            void setUpContext() {
                team = createTeam("팀이름");
                title = "재고";
            }

            @Test
            @DisplayName("Inventory 를 반환한다.")
            void it_returns_Inventory() {
                var target = mapper.toInventory(team, title);
                assertThat(target.getTeam()).isEqualTo(team);
                assertThat(target.getTitle()).isEqualTo(title);
                assertThat(target.getView()).isEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayName("toAddInventoryResponse 메서드는")
    class Describe_toAddInventoryResponse {
        @Nested
        @DisplayName("Long, List<Long> 를 받아")
        class Context_with_inventoryId_productIds {
            Long inventoryId;
            List<Long> productIds;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                productIds = List.of(1L, 2L, 3L, 4L, 5L);
            }

            @Test
            @DisplayName("AddInventoryResponse 를 반환한다.")
            void it_returns_AddInventoryResponse() {
                var target = mapper.toAddInventoryResponse(inventoryId, productIds);
                assertThat(target.inventoryId()).isEqualTo(inventoryId);
                IntStream.range(0, target.productIds()
                                .size())
                        .forEach(i -> assertThat(target.productIds()
                                .get(i)).isEqualTo(productIds.get(i)));
            }
        }
    }
}