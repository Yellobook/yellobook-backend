package com.yellobook.domain.MentionTest;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.entity.QMember;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.QParticipant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepositoryCustomImpl;
import com.yellobook.enums.MemberRole;
import com.yellobook.enums.MemberTeamRole;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.types.ExpressionUtils.allOf;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@Transactional
public class MentionTest {
    @Mock
    private JPAQueryFactory queryFactory;
    @Mock
    private JPAQuery<Participant> jpaQuery;

    @InjectMocks
    private ParticipantRepositoryCustomImpl participantRepositoryCustomImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findMentionsByNamePrefix_shouldReturnParticipants() {
        // Given
        String prefix = "진";
        Long teamId = 1L;

        QMember member = QMember.member;
        QParticipant participant = QParticipant.participant;

        Team team = new Team(1L, "nike", "010", "경기도");
        Member member1 = new Member(1L, "홍길동", "tj@tj.com", "http", MemberRole.USER);
        Member member2 = new Member(2L, "홍명보", "tjf@tj.com", "http", MemberRole.USER);
        Member member3 = new Member(3L, "신짱구", "tjfd@tj.com", "http", MemberRole.USER);
        Member member4 = new Member(4L, "김홍진", "tjf@ftj.com", "http", MemberRole.USER);
        Member member5 = new Member(5L, "홍김보", "tjf@tej.com", "http", MemberRole.USER);
        Member member6 = new Member(6L, "상당김", "tjf@tjh.com", "http", MemberRole.USER);

        Participant participant1 = new Participant(1L, team, member1, MemberTeamRole.ADMIN);
        Participant participant2 = new Participant(2L, team, member2, MemberTeamRole.ORDERER);
        Participant participant3 = new Participant(3L, team, member3, MemberTeamRole.ORDERER);
        Participant participant4 = new Participant(4L, team, member4, MemberTeamRole.ORDERER);
        Participant participant5 = new Participant(5L, team, member5, MemberTeamRole.ORDERER);
        Participant participant6 = new Participant(6L, team, member6, MemberTeamRole.ORDERER);

        // Mocking the behavior of JPAQueryFactory
        List<Participant> allParticipants = List.of(
                participant1, participant2, participant3, participant4, participant5, participant6
        );

        when(queryFactory.selectFrom(participant)).thenReturn(jpaQuery);
        when(jpaQuery.join(participant.member, member)).thenReturn(jpaQuery);
        when(jpaQuery.where(
                        member.nickname.like(prefix+"%")
                                .and(participant.team.id.eq(teamId))

        )).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenAnswer(invocation -> {
            return allParticipants.stream()
                    .filter(p -> p.getMember().getNickname().startsWith(prefix) && p.getTeam().getId().equals(teamId))
                    .collect(Collectors.toList());
        });
        // When
        List<Participant> participants = participantRepositoryCustomImpl.findMentionsByNamePrefix(prefix, teamId);

        // Then
        assertThat(participants).isEmpty();
        AssertionsForClassTypes.assertThat(participants.size()).isEqualTo(0);
        AssertionsForClassTypes.assertThat(participants.get(0).getMember().getNickname()).isEqualTo("홍길동");
        AssertionsForClassTypes.assertThat(participants.get(1).getMember().getNickname()).isEqualTo("홍명보");

    }
}
