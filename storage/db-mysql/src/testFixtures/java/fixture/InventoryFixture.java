package fixture;

import com.yellobook.core.domains.inventory.entity.Inventory;
import com.yellobook.core.domains.inventory.entity.Product;
import com.yellobook.core.domains.team.entity.Team;

public class InventoryFixture {
    // 재고
    private static final String INVENTORY_TITLE = "2024년 08월 06일 재고현황";

    // 상품
    private static final String PRODUCT_NAME = "제품";
    private static final String PRODUCT_SUBPRODUCT = "서브제품";
    private static final int PRODUCT_SKU = 1;
    private static final int PRODUCT_PURCHASE_PRICE = 100;
    private static final int PRODUCT_SALE_PRICE = 150;
    private static final int PRODUCT_AMOUNT = 100;

    public static Inventory createInventory(Team team) {
        return createInventory(INVENTORY_TITLE, team);
    }

    public static Inventory createInventory(String title, Team team) {
        return Inventory.builder()
                .title(title)
                .team(team)
                .build();
    }

    public static Product createProduct(Inventory inventory) {
        return createProduct(PRODUCT_NAME, PRODUCT_SUBPRODUCT, PRODUCT_SKU, PRODUCT_PURCHASE_PRICE, PRODUCT_SALE_PRICE,
                PRODUCT_AMOUNT, inventory);
    }

    public static Product createProduct(Inventory inventory, Integer amount) {
        return createProduct(PRODUCT_NAME, PRODUCT_SUBPRODUCT, PRODUCT_SKU, PRODUCT_PURCHASE_PRICE, PRODUCT_SALE_PRICE,
                amount, inventory);

    }

    public static Product createProduct(String name, String subProduct, Inventory inventory) {
        return createProduct(name, subProduct, PRODUCT_SKU, PRODUCT_PURCHASE_PRICE, PRODUCT_SALE_PRICE, PRODUCT_AMOUNT,
                inventory);
    }

    public static Product createProduct(String name, String subProduct, int sku, int purchasePrice, int salePrice,
                                        int amount, Inventory inventory) {
        return Product.builder()
                .name(name)
                .subProduct(subProduct)
                .sku(sku)
                .purchasePrice(purchasePrice)
                .salePrice(salePrice)
                .amount(amount)
                .inventory(inventory)
                .build();
    }
}

