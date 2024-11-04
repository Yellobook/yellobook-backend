package com.yellobook.domains.inventory.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.dto.query.QueryProductName;
import com.yellobook.domains.inventory.dto.query.QuerySubProduct;
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
}