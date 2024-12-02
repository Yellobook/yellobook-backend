package com.yellobook.core.api.common.validation.validator;

import com.yellobook.core.api.common.validation.annotation.ExistInform;
import com.yellobook.core.domains.inform.repository.InformRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistInformValidator implements ConstraintValidator<ExistInform, Long> {

    private final InformRepository informRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return informRepository.existsById(value);
    }
}
