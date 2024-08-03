package com.yellobook.common.validation.validator;

import com.yellobook.common.validation.annotation.ExistInventory;
import com.yellobook.domain.inventory.service.InventoryQueryService;
import com.yellobook.error.code.InventoryErrorCode;
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
        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(InventoryErrorCode.INVENTORY_NOT_FOUND.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
