package com.yellobook.common.validator;

import com.yellobook.common.annotation.ExistInform;
import com.yellobook.domain.inform.service.InformQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistInformValidator implements ConstraintValidator<ExistInform, String> {

    private final InformQueryService informQueryService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return true;
    }
}
