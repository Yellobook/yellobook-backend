package com.yellobook.domains.inform.service;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.inform.dto.request.CreateInformCommentRequest;
import com.yellobook.domains.inform.dto.request.CreateInformRequest;
import com.yellobook.domains.inform.dto.response.CreateInformCommentResponse;
import com.yellobook.domains.inform.dto.response.CreateInformResponse;
import com.yellobook.domains.inform.mapper.CommentMapper;
import com.yellobook.domains.inform.mapper.InformMapper;
import com.yellobook.domains.team.service.TeamQueryService;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.inform.repository.InformCommentRepository;
import com.yellobook.domains.inform.repository.InformMentionRepository;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.error.code.InformErrorCode;
import com.yellobook.error.code.MemberErrorCode;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InformCommandService {
    private final InformRepository informRepository;
    private final InformMapper informMapper;
    private final ParticipantRepository participantRepository;
    private final RedisTeamService redisService;
    private final TeamQueryService teamQueryService;
    private final MemberRepository memberRepository;
    private final InformMentionRepository informMentionRepository;
    private final InformCommentRepository informCommentRepository;
    private final CommentMapper commentMapper;

    public CreateInformResponse createInform(
            Long memberId,
            CreateInformRequest request
    ) {
        Long teamId = redisService.getCurrentTeamMember(memberId).getTeamId();

        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.error("[INFORM_ERROR] Redis 에서 조회한 사용자가 속한 팀 정보에 해당하는 쿼리 결과가 존재하지 않음: 사용자 ID_{} 팀 ID_{}", memberId, teamId);
                    return new CustomException(TeamErrorCode.TEAM_NOT_FOUND);
                });
        Member member = participant.getMember();
        Team team = participant.getTeam();

        Inform newInform = informMapper.toInform(request, member, team);
        informRepository.save(newInform);
        List<Long> mentionIds = request.mentionIds();

        // 언급한 사용자들이 존재할 경우
        if (mentionIds != null && !mentionIds.isEmpty()) {
            List<InformMention> mentions = request.mentionIds().stream().map(mentionDTO -> {
                Member mentioned = memberRepository.findById(mentionDTO)
                        .orElseThrow(() -> new CustomException(TeamErrorCode.MENTIONED_MEMBER_NOT_FOUND));
                return informMapper.toInformMention(newInform, mentioned);
            }).toList();
            informMentionRepository.saveAll(mentions);
        }
        log.info("[INFORM_INFO] 사용자 ID_{} 가 공지및일정 ID_{} 생성", member.getId(), newInform.getId());
        return informMapper.toCreateInformResponse(newInform);
    }

    public void deleteInform(Long informId, Long memberId) {
        Inform inform = informRepository.findById(informId).get();
        // 작성자가 아닐 경우
        if (!inform.getMember().getId().equals(memberId)) {
            log.warn("[INFORM_WARN] 본인이 작성하지 않은 공지 및 일정 ID_{} 에 대해 사용자 ID_{} 가 삭제 요청", informId, memberId);
            throw new CustomException(InformErrorCode.INFORM_MEMBER_NOT_MATCH);
        }

        log.info("[INFORM_INFO] 공지및 일정 ID_{} 삭제", inform.getId());
        informRepository.deleteById(informId);
    }

    public void increaseViewCount(Long informId, TeamMemberVO teamMemberVO){
        Inform inform = informRepository.findById(informId)
                .orElseThrow(() -> new CustomException(InformErrorCode.INFORM_NOT_FOUND));

        List<InformMention> mentions = informMentionRepository.findByInformId(informId);
        boolean isMentioned = mentions
                .stream()
                .anyMatch(mention -> mention.getMember().getId().equals(teamMemberVO.getMemberId()));

        // 언급된 사용자이거나, 본인이 작성했을 경우
        if(isMentioned || inform.getMember().getId().equals(teamMemberVO.getMemberId())){
            inform.updateView();
        }
        else{
            throw new CustomException(InformErrorCode.NOT_MENTIONED);
        }
    }


    public CreateInformCommentResponse addComment(
            Long informId,
            Long memberId,
            CreateInformCommentRequest request) {
        Inform inform = informRepository.findById(informId).get();

        List<InformMention> mentions = informMentionRepository.findByInformId(informId);

        Long writerId = inform.getMember().getId();
        boolean isMentioned = mentions.stream().anyMatch(mention -> mention.getMember().getId().equals(memberId));

        // 언급된 사용자이거나, 본인이 작성했을 경우
        if (memberId.equals(writerId) || isMentioned) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

            InformComment newComment = InformComment.builder()
                    .inform(inform)
                    .member(member)
                    .content(request.content())
                    .build();

            informCommentRepository.save(newComment);

            return commentMapper.toCreateInformCommentResponse(newComment);
        } else {
            throw new CustomException(InformErrorCode.NOT_MENTIONED);
        }
    }
}