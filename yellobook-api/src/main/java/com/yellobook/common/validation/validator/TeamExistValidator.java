package com.yellobook.common.validation.validator;

import com.yellobook.common.validation.annotation.ExistTeam;
import com.yellobook.domain.team.service.TeamQueryService;
import com.yellobook.error.code.TeamErrorCode;
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
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
