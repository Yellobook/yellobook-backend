package com.yellobook.common.validation.validator;

import com.yellobook.common.validation.annotation.ExistProduct;
import com.yellobook.domains.inventory.service.InventoryQueryService;
import com.yellobook.error.code.InventoryErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistProductValidator implements ConstraintValidator<ExistProduct, Long> {
    private final InventoryQueryService inventoryQueryService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = inventoryQueryService.existByProductId(value);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(InventoryErrorCode.PRODUCT_NOT_FOUND.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
