package com.yellobook.domain.inform.service;

import com.yellobook.common.utils.TeamUtil;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inform.dto.InformCommentRequest;
import com.yellobook.domain.inform.dto.InformRequest;
import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domain.inform.mapper.InformMapper;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InformCommandServiceImpl implements InformCommandService {
    private final InformRepository informRepository;
    private final InformMapper informMapper;
    private final ParticipantRepository participantRepository;
    private final TeamUtil teamUtil;
    @Override
    public InformResponse.CreateInformResponseDTO createInform(CustomOAuth2User oAuth2User, InformRequest.CreateInformRequestDTO request){
        Long memberId = oAuth2User.getMemberId();
        Long teamId = teamUtil.getCurrentTeam(memberId);

        Participant participant = participantRepository.findByMemberIdAndTeamId(memberId, teamId)
                .orElseThrow(() ->{
                    log.error("Member {} does not exist in team {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.TEAM_NOT_FOUND);
                });
        Member member = participant.getMember();
        Team team = participant.getTeam();

        Inform newInform = informMapper.toInform(request, member, team);
        informRepository.save(newInform);

        log.info("Created new inform {}", newInform.getId());

        return informMapper.toCreateInformResponseDTO(newInform);
    }

    @Override
    public void deleteInform(Long informId, CustomOAuth2User oAuth2User){

    }

    @Override
    public InformComment createComment(Long memberId, CustomOAuth2User oauth2User, InformCommentRequest.PostCommentRequestDTO request){
        return null;
    }
}
