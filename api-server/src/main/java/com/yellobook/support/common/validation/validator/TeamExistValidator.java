package com.yellobook.support.common.validation.validator;

import com.yellobook.support.common.validation.annotation.ExistTeam;
import com.yellobook.support.error.code.TeamErrorCode;
import com.yellobook.team.service.TeamQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamExistValidator implements ConstraintValidator<ExistTeam, Long> {
    private final TeamQueryService teamQueryService;

    @Override
    public boolean isValid(Long teamId, ConstraintValidatorContext context) {
        boolean isValid = teamQueryService.existsByTeamId(teamId);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            TeamErrorCode.TEAM_NOT_FOUND.toString()
                    )
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
