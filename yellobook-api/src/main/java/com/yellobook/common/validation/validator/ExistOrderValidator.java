package com.yellobook.common.validation.validator;

import com.yellobook.common.validation.annotation.ExistOrder;
import com.yellobook.domains.order.service.OrderQueryService;
import com.yellobook.error.code.OrderErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistOrderValidator implements ConstraintValidator<ExistOrder, Long> {
    private final OrderQueryService orderQueryService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = orderQueryService.existsByOrderId(value);

        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    OrderErrorCode.ORDER_NOT_FOUND.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
