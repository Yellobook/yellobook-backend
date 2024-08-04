package com.yellobook.domain.team.service;

import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domain.inform.dto.MentionDTO;
import com.yellobook.domain.team.mapper.ParticipantMapper;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.ParticipantRepositoryCustom;
import com.yellobook.domains.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    @Override
    public Team findByTeamId(Long teamId, Long memberId){
        return null;
    }

    @Override
    public List<MentionDTO> searchParticipants(Long teamId, String name){
        List<Participant> mentions;

        if(name.equalsIgnoreCase("@everyone")){
            mentions = participantRepository.findAllByTeamId(teamId);
        }
        else if(name.startsWith("@")){
            String prefix = name.substring(1);
            mentions = participantRepository.findMentionsByNamePrefix(prefix, teamId);
        }
        else {
            return List.of();
        }
        return participantMapper.toMentionDTOlist(mentions);
    }
}
