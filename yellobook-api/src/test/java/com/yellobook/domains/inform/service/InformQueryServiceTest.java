package com.yellobook.domains.inform.service;

import com.yellobook.domains.inform.dto.response.GetInformResponse;
import com.yellobook.domains.inform.dto.response.GetInformResponse.CommentItem;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.inform.mapper.InformMapper;
import com.yellobook.domains.inform.repository.InformMentionRepository;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.error.code.InformErrorCode;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InformQueryServiceTest {
    @Mock
    private InformRepository informRepository;
    @Mock
    private InformMapper informMapper;
    @Mock
    private InformMentionRepository informMentionRepository;

    @InjectMocks
    private InformQueryService informQueryService;

    Inform inform;
    Member mentionedMember;
    Member author;
    Member nonMentionedMember;

    @BeforeEach
    void setUp() {
        inform = mock(Inform.class);
        mentionedMember = mock(Member.class);
        author = mock(Member.class);
        nonMentionedMember = mock(Member.class);
    }

    @Nested
    @DisplayName("getInformById 메소드는")
    class Describe_getInformById{

        @Nested
        @DisplayName("inform이 존재하지만 mention되지 않은 경우")
        class Context_Exist_Inform_But_Not_Mentioned{

            CustomException exception;

            @BeforeEach
            void setUp() {
                when(inform.getId()).thenReturn(1L);
                when(informRepository.findById(inform.getId())).thenReturn(Optional.of(inform));

                when(inform.getMember()).thenReturn(author);
                when(author.getId()).thenReturn(1L);
                when(mentionedMember.getId()).thenReturn(2L);
                when(nonMentionedMember.getId()).thenReturn(99L);

                when(inform.getComments()).thenReturn(List.of(
                        InformComment.builder().content("test").inform(inform).member(mentionedMember).build()
                ));
                when(informMentionRepository.findByInformId(inform.getId())).thenReturn(List.of(
                        new InformMention(inform, mentionedMember)
                ));

                exception = assertThrows(CustomException.class, ()->{
                   informQueryService.getInformById(nonMentionedMember.getId(), inform.getId());
                });
            }

            @Test
            @DisplayName("NOT_MENTIONED에러를 반환한다.")
            void it_returns_not_mentioned(){
                assertEquals(InformErrorCode.NOT_MENTIONED.getMessage() ,exception.getErrorCode().getMessage());
            }
        }

        @Nested
        @DisplayName("inform이 존재하며 mention된 경우")
        class Context_Exist_Inform_And_Mentioned{

            GetInformResponse response;

            @BeforeEach
            void setUp() {
                when(inform.getId()).thenReturn(1L);
                when(informRepository.findById(inform.getId())).thenReturn(Optional.of(inform));
                when(inform.getMember()).thenReturn(author);
                when(author.getId()).thenReturn(1L);
                when(mentionedMember.getId()).thenReturn(2L);

                when(inform.getComments()).thenReturn(List.of(
                        InformComment.builder().content("test").inform(inform).member(mentionedMember).build()
                ));
                when(informMentionRepository.findByInformId(inform.getId())).thenReturn(List.of(
                        new InformMention(inform, mentionedMember)
                ));

                when(informMapper.toGetInformResponseDTO(inform, inform.getComments(), List.of(mentionedMember)))
                        .thenReturn(new GetInformResponse("test", "test",
                                List.of(new GetInformResponse.MentionItem(2L,"test")),
                                10,
                                List.of(new CommentItem(1L, 2L, "test", LocalDateTime.now())), LocalDate.now()));
            }

            @Test
            @DisplayName("inform을 반환한다.")
            void it_returns_inform(){
                response = informQueryService.getInformById(mentionedMember.getId(), inform.getId());

                assertNotNull(response);
            }
        }

        @Nested
        @DisplayName("inform이 존재하고 작성자인 경우")
        class Context_Exist_Inform_And_Author {
            GetInformResponse response;

            @BeforeEach
            void setUp() {
                when(inform.getId()).thenReturn(1L);
                when(informRepository.findById(inform.getId())).thenReturn(Optional.of(inform));
                when(inform.getMember()).thenReturn(author);
                when(author.getId()).thenReturn(1L);

                when(inform.getComments()).thenReturn(List.of(
                        InformComment.builder().content("test").inform(inform).member(author).build()
                ));
                when(informMentionRepository.findByInformId(inform.getId())).thenReturn(List.of(
                        new InformMention(inform, author)
                ));

                when(informMapper.toGetInformResponseDTO(inform, inform.getComments(), List.of(author)))
                        .thenReturn(new GetInformResponse("test", "test",
                                List.of(new GetInformResponse.MentionItem(1L, "test")),
                                10,
                                List.of(new CommentItem(1L, 1L, "test", LocalDateTime.now())), LocalDate.now()));
            }

            @Test
            @DisplayName("inform을 반환한다.")
            void it_returns_inform() {
                response = informQueryService.getInformById(author.getId(), inform.getId());

                assertNotNull(response);
            }
        }

        @Nested
        @DisplayName("inform이 존재하지 않은 경우")
        class Context_Not_Exist_Inform{
            NoSuchElementException exception;

            @BeforeEach
            void setUp() {
                when(informRepository.findById(anyLong())).thenReturn(Optional.empty());

                exception = assertThrows(NoSuchElementException.class, () -> {
                    informQueryService.getInformById(1L, 1L);  // 임의의 memberId와 informId를 사용
                });
            }

            @Test
            @DisplayName("NoSuchElementException 에러를 반환한다.")
            void it_returns_inform_not_found() {
                assertNotNull(exception);
            }
        }
    }

}
