package com.yellobook.core.api.common.validation.validator;

import com.yellobook.core.api.common.validation.annotation.ExistInventory;
import com.yellobook.core.api.domains.inventory.service.InventoryQueryService;
import com.yellobook.core.api.support.error.code.InventoryErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistInventoryValidator implements ConstraintValidator<ExistInventory, Long> {
    private final InventoryQueryService inventoryQueryService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = inventoryQueryService.existByInventoryId(value);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(InventoryErrorCode.INVENTORY_NOT_FOUND.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
