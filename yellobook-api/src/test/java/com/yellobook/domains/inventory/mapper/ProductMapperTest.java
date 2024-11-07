package com.yellobook.domains.inventory.mapper;

import static fixture.InventoryFixture.createInventory;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.domains.inventory.dto.cond.ExcelProductCond;
import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.dto.query.QueryProductName;
import com.yellobook.domains.inventory.dto.query.QuerySubProduct;
import com.yellobook.domains.inventory.dto.request.AddProductRequest;
import com.yellobook.domains.inventory.entity.Inventory;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("ProductMapper Unit Test")
class ProductMapperTest {
    ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Nested
    @DisplayName("toGetProductsResponse 메서드는")
    class Describe_toGetProductsResponse {
        @Nested
        @DisplayName("List<QueryProduct> 를 받아")
        class Context_with_products {
            List<QueryProduct> products;

            @BeforeEach
            void setUpContext() {
                products = List.of(
                        QueryProduct.builder()
                                .productId(1L)
                                .name("제품1")
                                .subProduct("하위제품1")
                                .sku(123123123)
                                .purchasePrice(1000)
                                .salePrice(200000000)
                                .build()
                );
            }

            @Test
            @DisplayName("GetProductsResponse를 반환한다.")
            void it_returns_GetProductsResponse() {
                var target = mapper.toGetProductsResponse(products);
                var targetProducts = target.products();
                assertThat(targetProducts.size()).isEqualTo(products.size());
                IntStream.range(0, products.size())
                        .forEach(i -> {
                            var sourceProduct = products.get(i);
                            var targetProduct = targetProducts.get(i);
                            assertThat(sourceProduct.productId()).isEqualTo(targetProduct.productId());
                            assertThat(sourceProduct.name()).isEqualTo(targetProduct.name());
                            assertThat(sourceProduct.subProduct()).isEqualTo(targetProduct.subProduct());
                            assertThat(sourceProduct.sku()).isEqualTo(targetProduct.sku());
                            assertThat(sourceProduct.purchasePrice()).isEqualTo(targetProduct.purchasePrice());
                            assertThat(sourceProduct.salePrice()).isEqualTo(targetProduct.salePrice());
                        });
            }
        }
    }


    @Nested
    @DisplayName("toGetProductsNameResponse 메서드는")
    class Describe_toGetProductsNameResponse {
        @Nested
        @DisplayName("List<QueryProductName> 를 받아")
        class Context_with_productNames {
            List<QueryProductName> productNames;

            @BeforeEach
            void setUpContext() {
                productNames = List.of(
                        QueryProductName.builder()
                                .name("제품1")
                                .build(),
                        QueryProductName.builder()
                                .name("제품2")
                                .build()
                );
            }

            @Test
            @DisplayName("GetProductsNameResponse를 반환한다.")
            void it_returns_GetProductsNameResponse() {
                var target = mapper.toGetProductsNameResponse(productNames);
                var targetNames = target.names();
                assertThat(targetNames.size()).isEqualTo(productNames.size());
                IntStream.range(0, productNames.size())
                        .forEach(i -> {
                            var sourceName = productNames.get(i)
                                    .name();
                            var targetName = targetNames.get(i);
                            assertThat(sourceName).isEqualTo(targetName);
                        });
            }
        }

        @Nested
        @DisplayName("빈 리스트를 받는 경우")
        class Context_with_emptyList {
            List<QueryProductName> productNames = Collections.emptyList();

            @Test
            @DisplayName("빈 리스트를 담은 GetProductsNameResponse를 반환한다.")
            void it_returns_GetProductsNameResponse_with_empty_list() {
                var target = mapper.toGetProductsNameResponse(productNames);
                var targetNames = target.names();
                assertThat(targetNames).isInstanceOf(List.class);
                assertThat(targetNames).isEmpty();
            }
        }

    }

    @Nested
    @DisplayName("toGetSubProductNameResponse 메서드는")
    class Describe_toGetSubProductNameResponse {
        @Nested
        @DisplayName("List<QuerySubProduct> 를 받아")
        class Context_with_productNames {
            List<QuerySubProduct> subProducts;

            @BeforeEach
            void setUpContext() {
                subProducts = List.of(
                        QuerySubProduct.builder()
                                .subProductName("하위제품1")
                                .productId(2L)
                                .build(),
                        QuerySubProduct.builder()
                                .subProductName("하위제품2")
                                .productId(2L)
                                .build()
                );
            }

            @Test
            @DisplayName("GetSubProductNameResponse를 반환한다.")
            void it_returns_GetProductsNameResponse() {
                var target = mapper.toGetSubProductNameResponse(subProducts);
                var targetSubProducts = target.subProducts();
                assertThat(targetSubProducts.size()).isEqualTo(subProducts.size());
                IntStream.range(0, targetSubProducts.size())
                        .forEach(i -> {
                            var sourceSubProduct = subProducts.get(i);
                            var targetSubProduct = targetSubProducts.get(i);
                            assertThat(targetSubProduct.productId()).isEqualTo(sourceSubProduct.productId());
                            assertThat(targetSubProduct.subProductName()).isEqualTo(sourceSubProduct.subProductName());
                        });
            }
        }

        @Nested
        @DisplayName("빈 리스트를 받는 경우")
        class Context_with_emptyList {
            List<QuerySubProduct> subProducts = Collections.emptyList();

            @Test
            @DisplayName("빈 리스트를 담은 GetSubProductNameResponse를 반환한다.")
            void it_returns_GetProductsNameResponse_with_empty_list() {
                var target = mapper.toGetSubProductNameResponse(subProducts);
                assertThat(target).isNotNull();
                assertThat(target.subProducts()).isInstanceOf(List.class);
                assertThat(target.subProducts()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("toProduct 메서드는")
    class Describe_toProduct {
        @Nested
        @DisplayName("AddProductRequest, Inventory 를 받아")
        class Context_with_request_inventory {
            AddProductRequest request;
            Inventory inventory;

            @BeforeEach
            void setUpContext() {
                request = AddProductRequest.builder()
                        .name("운동화")
                        .subProduct("흰색")
                        .sku(123)
                        .purchasePrice(1000)
                        .salePrice(1500)
                        .amount(10)
                        .build();
                inventory = createInventory(null);
            }

            @Test
            @DisplayName("Product를 반환한다.")
            void it_returns_Product() {
                var target = mapper.toProduct(request, inventory);
                assertThat(target.getName()).isEqualTo(request.name());
                assertThat(target.getSubProduct()).isEqualTo(request.subProduct());
                assertThat(target.getSku()).isEqualTo(request.sku());
                assertThat(target.getPurchasePrice()).isEqualTo(request.purchasePrice());
                assertThat(target.getSalePrice()).isEqualTo(request.salePrice());
                assertThat(target.getAmount()).isEqualTo(request.amount());
                assertThat(target.getInventory()).isEqualTo(inventory);
            }
        }

        @Nested
        @DisplayName("ExcelProductCond, Inventory 를 받아")
        class Context_with_productCond_inventory {
            ExcelProductCond cond;
            Inventory inventory;

            @BeforeEach
            void setUpContext() {
                cond = ExcelProductCond.builder()
                        .name("운동화")
                        .subProduct("흰색")
                        .sku(123)
                        .purchasePrice(1000)
                        .salePrice(1500)
                        .amount(10)
                        .build();
                inventory = createInventory(null);
            }

            @Test
            @DisplayName("Product를 반환한다.")
            void it_returns_Product() {
                var target = mapper.toProduct(cond, inventory);
                assertThat(target.getName()).isEqualTo(cond.name());
                assertThat(target.getSubProduct()).isEqualTo(cond.subProduct());
                assertThat(target.getSku()).isEqualTo(cond.sku());
                assertThat(target.getPurchasePrice()).isEqualTo(cond.purchasePrice());
                assertThat(target.getSalePrice()).isEqualTo(cond.salePrice());
                assertThat(target.getAmount()).isEqualTo(cond.amount());
                assertThat(target.getInventory()).isEqualTo(inventory);
            }
        }
    }

    @Nested
    @DisplayName("toAddProductResponse 메서드는")
    class Describe_toAddProductResponse {
        @Nested
        @DisplayName("Long 을 받아")
        class Context_with_productId {
            Long productId;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
            }

            @Test
            @DisplayName("AddProductResponse를 반환한다.")
            void it_returns_AddProductResponse() {
                var target = mapper.toAddProductResponse(productId);
                assertThat(target.productId()).isEqualTo(productId);
            }
        }
    }

    @Nested
    @DisplayName("toProductInfo 메서드는")
    class Describe_toProductInfo {
        @Nested
        @DisplayName("List<QueryProduct> 를 받아")
        class Context_with_products {
            List<QueryProduct> products;

            @BeforeEach
            void setUpContext() {
                products = List.of(
                        QueryProduct.builder()
                                .productId(1L)
                                .name("제품 A")
                                .subProduct("하위 제품 A")
                                .sku(1001)
                                .purchasePrice(1500)
                                .salePrice(2000)
                                .amount(5)
                                .build(),
                        QueryProduct.builder()
                                .productId(2L)
                                .name("제품 B")
                                .subProduct("하위 제품 B")
                                .sku(1002)
                                .purchasePrice(2500)
                                .salePrice(3000)
                                .amount(10)
                                .build()
                );
            }

            @Test
            @DisplayName("List<ProductInfo> 를 반환한다.")
            void it_returns_ProductInfo_list() {
                var target = mapper.toProductInfo(products);
                IntStream.range(0, target.size())
                        .forEach(i -> {
                            var targetProduct = target.get(i);
                            var sourceProduct = products.get(i);
                            assertThat(targetProduct.productId()).isEqualTo(sourceProduct.productId());
                            assertThat(targetProduct.name()).isEqualTo(sourceProduct.name());
                            assertThat(targetProduct.subProduct()).isEqualTo(sourceProduct.subProduct());
                            assertThat(targetProduct.sku()).isEqualTo(sourceProduct.sku());
                            assertThat(targetProduct.purchasePrice()).isEqualTo(sourceProduct.purchasePrice());
                            assertThat(targetProduct.salePrice()).isEqualTo(sourceProduct.salePrice());
                            assertThat(targetProduct.amount()).isEqualTo(sourceProduct.amount());
                        });
            }
        }
    }

    @Nested
    @DisplayName("toSubProductInfo 메서드는")
    class Describe_toSubProductInfo {
        @Nested
        @DisplayName("List<QuerySubProduct> 를 받아")
        class Context_with_subProducts {
            List<QuerySubProduct> subProducts;

            @BeforeEach
            void setUpContext() {
                subProducts = List.of(
                        QuerySubProduct.builder()
                                .productId(1L)
                                .subProductName("하위 제품 A")
                                .build(),
                        QuerySubProduct.builder()
                                .productId(2L)
                                .subProductName("하위 제품 B")
                                .build(),
                        QuerySubProduct.builder()
                                .productId(2L)
                                .subProductName("하위 제품 C")
                                .build()
                );
            }

            @Test
            @DisplayName("List<SubProductInfo>를 반환한다.")
            void it_returns_SubProductInfo_list() {
                var target = mapper.toSubProductInfo(subProducts);
                IntStream.range(0, target.size())
                        .forEach(i -> {
                            var targetSubProduct = target.get(i);
                            var sourceSubProduct = subProducts.get(i);
                            assertThat(targetSubProduct.productId()).isEqualTo(sourceSubProduct.productId());
                            assertThat(targetSubProduct.subProductName()).isEqualTo(sourceSubProduct.subProductName());
                        });
            }
        }
    }
}