package com.yellobook.core.domain.team;

public class TeamValidator {

    private final TeamRepository teamRepository;
    private final TeamRoleVerifier teamRoleVerifier;

    public TeamValidator(TeamRepository teamRepository, TeamRoleVerifier teamRoleVerifier) {
        this.teamRepository = teamRepository;
        this.teamRoleVerifier = teamRoleVerifier;
    }

    /*
    TODO: controller 단으로 넘길 것
     */
    public boolean isValidCreation(String name, String phoneNumber, String address) {
        return name != null && !name.isEmpty() && phoneNumber != null && !phoneNumber.isEmpty() && address != null
                && !address.isEmpty() && !teamRepository.existByName(name);
    }
}
