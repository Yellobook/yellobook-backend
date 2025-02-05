//package com.yellobook.api.controller.inform.controller;
//
//import com.yellobook.api.controller.inform.dto.request.CreateInformCommentRequest;
//import com.yellobook.api.controller.inform.dto.request.CreateInformRequest;
//import com.yellobook.api.controller.inform.dto.response.CreateInformCommentResponse;
//import com.yellobook.api.controller.inform.dto.response.CreateInformResponse;
//import com.yellobook.api.controller.inform.dto.response.GetInformResponse;
//import com.yellobook.api.support.ApiMember;
//import com.yellobook.api.support.ApiCurrentTeam;
//import com.yellobook.core.domain.inform.InformService;
//import com.yellobook.api.support.TeamMember;
//import com.yellobook.support.common.validation.annotation.ExistInform;
//import com.yellobook.support.response.ResponseFactory;
//import com.yellobook.support.response.SuccessResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@Validated
//@RequestMapping("/api/v1/informs")
//@Tag(name = "\uD83D\uDCBC 공지", description = "Inform API")
//public class InformController {
//    private final InformService informService;
//
//    public InformController(InformService informService) {
//        this.informService = informService;
//    }
//
//    @PostMapping
//    @Operation(summary = "공지 작성", description = "새로운 공지를 생성하는 API 입니다.")
//    public ResponseEntity<SuccessResponse<CreateInformResponse>> createInform(
//            @RequestBody CreateInformRequest request,
//    ) {
//        var result = informService.createInform(teamMemberVO.getMemberId(), request);
//        return ResponseFactory.created(result);
//    }
//
//    @DeleteMapping("/{informId}")
//    @Operation(summary = "공지 삭제", description = "등록된 공지를 삭제하는 API 입니다.")
//    public ResponseEntity<Void> deleteInform(
//            @ExistInform @PathVariable("informId") Long informId,
//            @ApiMember ApiMember apiMember
//            ) {
//        informCommandService.deleteInform(informId,apiMember.toAuthor());
//        return ResponseFactory.noContent();
//    }
//
//
//    // 조회의 경우에는 mentionedMember 랑 Author 둘다 접근이 가능하니까
//    // 여기서 ApiMember 넘어온 이 Member 가 Author 인지, MentionedMember 인지 몰라요..
//    // AuthorOrMentionedMember VO 를 하나 더 만들어야 하냐?
//    // 컨트롤러에서는 완전한 Member 객체, Team 객체로 넘겨줄 수 있어요
//    // 뭘로변환해주냐?
//    @GetMapping("/{informId}")
//    @Operation(summary = "공지 조회", description = "등록된 공지를 조회하는 API 입니다.")
//    public ResponseEntity<SuccessResponse<GetInformResponse>> getInform(
//            @ExistInform @PathVariable("informId") Long informId,
//            @ApiMember ApiMember apiMember
//            @ApiCurrentTeam ApiCurrentTeam apiTeam
//            ) {
//        var result = informQueryService.getInformById(, informId);
//        return ResponseFactory.success(result);
//    }
//
//    // Author 나 MentionedMember 만 준영속으로 이용하니까, id 만 있으면 되는데, Author 나 MentionedMember 만 영속성계층에 넘겨주면 되지않냐
//    // Member 를 읽어오는건 MemberReader 에 있음
//    // Member Reader 에서는 Member 도메인 엔티티로 변환을 해줄텐데
//    // 얘를 InformReader 에서 주입받아서 이용하게되면 어차피 Inform 에는 Member 도메인 객체가 들어갈 수밖에 없음
//    //
//
//
//    @PatchMapping("/{informId}/views")
//    @Operation(summary = "조회수 증가", description = "조회하는 공지의 조회수 증가 API입니다.")
//    public ResponseEntity<Void> increaseInformView(
//            @ExistInform @PathVariable("informId") Long informId,
//            @TeamMember TeamMemberVO teamMemberVO
//    ) {
//        informCommandService.increaseViewCount(informId, teamMemberVO);
//        return ResponseFactory.noContent();
//    }
//
//
//    @PostMapping("/{informId}/comment")
//    @Operation(summary = "공지 댓글 작성", description = "공지에 댓글을 작성하는 API 입니다.")
//    public ResponseEntity<SuccessResponse<CreateInformCommentResponse>> addComment(
//            @ExistInform @PathVariable("informId") Long informId,
//            @TeamMember TeamMemberVO teamMemberVO,
//            @RequestBody CreateInformCommentRequest request
//    ) {
//        var result = informCommandService.addComment(informId, teamMemberVO.getMemberId(), request);
//        return ResponseFactory.created(result);
//    }
//}
