package com.yellobook.core.domain.team;

import org.springframework.stereotype.Component;

@Component
public class TeamWriter {
    private final TeamRepository teamRepository;


    public TeamWriter(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /*
    팀 생성
     */
    public Long create(String name, String description, String phoneNumber, String address, Boolean searchable) {
        return teamRepository.save(name, description, phoneNumber, address, searchable);
    }


    /**
     * 팀 공개 여부 수정
     */
    public void updateSearchable(Long teamId, Boolean searchable) {
        teamRepository.updateSearchable(teamId, searchable);
    }

}
