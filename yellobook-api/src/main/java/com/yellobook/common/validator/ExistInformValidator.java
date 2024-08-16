package com.yellobook.common.validator;

import com.yellobook.common.annotation.ExistInform;
import com.yellobook.domains.inform.repository.InformRepository;
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
