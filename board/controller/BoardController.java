package com.ssafy.undaid.domain.board.controller;

import com.ssafy.undaid.domain.board.dto.request.BoardCreateRequestDto;
import com.ssafy.undaid.domain.board.dto.request.BoardUpdateRequestDto;
import com.ssafy.undaid.domain.board.dto.response.BoardCreateResponseDto;
import com.ssafy.undaid.domain.board.dto.response.BoardDetailResponseDto;
import com.ssafy.undaid.domain.board.dto.response.BoardListResponseDto;
import com.ssafy.undaid.domain.board.service.BoardService;
import com.ssafy.undaid.global.common.exception.BaseException;
import com.ssafy.undaid.global.common.response.ApiDataResponse;
import com.ssafy.undaid.global.common.response.ApiResponse;
import com.ssafy.undaid.global.common.response.HttpStatusCode;
import com.ssafy.undaid.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssafy.undaid.global.common.exception.ErrorCode.TOKEN_VALIDATION_FAILED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {

    private final JwtTokenProvider jwtTokenProvider;
    private final BoardService boardService;

    @PostMapping("/admin/board")
    public ApiDataResponse<?> AdminCreateBoard(@RequestBody BoardCreateRequestDto boardCreateRequestDto, HttpServletRequest request){

        //jwt 토큰 유효성 검사
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new BaseException(TOKEN_VALIDATION_FAILED);
        }

        //jwt 토큰에서 id 추출
        int userId = jwtTokenProvider.getClaims(token).get("userId", Integer.class);

        BoardCreateResponseDto result=boardService.createBoard(boardCreateRequestDto, userId);
        return new ApiDataResponse<>(HttpStatusCode.CREATED, result,"게시글 작성 완료");
    }

    //게시글 작성
    @PostMapping("/board")
    public ApiDataResponse<?> createBoard(@RequestBody BoardCreateRequestDto boardCreateRequestDto, @RequestHeader("Authorization") String authorizationHeader){

        // 1. 헤더에서 JWT 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");

        // 2. 토큰 유효성 검사
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new BaseException(TOKEN_VALIDATION_FAILED);
        }

        int userId = jwtTokenProvider.getClaims(token).get("userId", Integer.class);
        BoardCreateResponseDto result=boardService.createBoard(boardCreateRequestDto, userId);
        return new ApiDataResponse<>(HttpStatusCode.CREATED, result,"게시글 작성 완료");
    }

    // 게시글 목록 조회
    @GetMapping("/board")
    public ApiDataResponse<?> getAllBoards(Pageable pageable) {
        Page<BoardListResponseDto> result = boardService.getAllBoards(pageable);
        return new ApiDataResponse<>(HttpStatusCode.OK, result,null);
    }

    // 특정 게시글 조회
    @GetMapping("/board/{boardId}")
    public ApiDataResponse<?> getBoard(@PathVariable("boardId") Integer boardId) {
        BoardDetailResponseDto result = boardService.getBoard(boardId);
        return new ApiDataResponse<>(HttpStatusCode.OK, result,null);
    }

    // 게시글 수정(관리자)
    @PatchMapping("/admin/board/{boardId}")
    public ResponseEntity<Void> adminUpdateBoard(
            @PathVariable("boardId") Integer boardId,
            @RequestBody BoardUpdateRequestDto boardUpdateRequestDto
    ) {
        boardService.updateBoard(boardId, boardUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    //게시글 수정

    @PatchMapping("/board/{boardId}")
    public ApiResponse updateBoard(
            @PathVariable("boardId") Integer boardId,
            @RequestBody BoardUpdateRequestDto boardUpdateRequestDto
    ) {
        boardService.updateBoard(boardId, boardUpdateRequestDto);
        return new ApiResponse(HttpStatusCode.OK.getIsSuccess(), HttpStatusCode.OK.getStatus(), "게시글 수정 완료");
    }

    //게시글 삭제(관리자)

    @DeleteMapping("/admin/board/{boardId}")
    public ResponseEntity<Void> adminDeleteBoard(@PathVariable("boardId") Integer boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("boardId") Integer boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }
}