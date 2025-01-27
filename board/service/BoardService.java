package com.ssafy.undaid.domain.board.service;

import com.ssafy.undaid.domain.board.dto.request.BoardCreateRequestDto;
import com.ssafy.undaid.domain.board.dto.request.BoardUpdateRequestDto;
import com.ssafy.undaid.domain.board.dto.response.BoardCreateResponseDto;
import com.ssafy.undaid.domain.board.dto.response.BoardDetailResponseDto;
import com.ssafy.undaid.domain.board.dto.response.BoardListResponseDto;
import com.ssafy.undaid.domain.board.entity.Board;
import com.ssafy.undaid.domain.board.entity.repository.BoardRepository;
import com.ssafy.undaid.domain.user.entity.Users;
import com.ssafy.undaid.domain.user.entity.repository.UserRepository;
import com.ssafy.undaid.global.common.exception.BaseException;
import com.ssafy.undaid.global.common.response.HttpStatusCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.ssafy.undaid.global.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    // 게시글 생성
    @Transactional
    public BoardCreateResponseDto createBoard(BoardCreateRequestDto boardCreateRequestDto, int userId) {
        // writerId를 사용해 Users 엔티티를 조회
        Users writer = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND));

        // Board 엔티티 생성
        Board board = Board.builder()
                .title(boardCreateRequestDto.getTitle())
                .content(boardCreateRequestDto.getContent())
                .category(boardCreateRequestDto.getCategory())
                .writer(writer) // Users 객체 설정
                .build();

        Board savedBoard=boardRepository.save(board);

        return BoardCreateResponseDto.builder()
                .boardId(savedBoard.getBoardId())
                .title(savedBoard.getTitle())
                .content(savedBoard.getContent())
                .category(savedBoard.getCategory())
                .createdAt(savedBoard.getCreatedAt())
                .updatedAt(savedBoard.getUpdatedAt())
                .build();

    }

    // 게시글 리스트 조회
    public Page<BoardListResponseDto> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(this::toDto);
    }

    // 게시글 상세 조회
    @Transactional
    public BoardDetailResponseDto getBoard(Integer boardId) {

        // 게시글 조회 // 에러 종류 맞나 의문
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(BAD_REQUEST_ERROR));

        // 조회수 증가
        boardRepository.incrementViewCount(boardId);

        // Board 객체를 DTO로 변환하여 반환
        return toDetailDto(board);
    }

    //게시글 수정
    @Transactional
    public void updateBoard(Integer boardId, BoardUpdateRequestDto boardUpdateRequestDto){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));
        board.update(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent());
    }

    //게시글 삭제
    @Transactional
    public void deleteBoard(Integer boardId) {
        boardRepository.deleteById(boardId);
    }

    // Board를 BoardDetailResponseDto로 변환
    private BoardDetailResponseDto toDetailDto(Board board) {
        return BoardDetailResponseDto.builder()
                .title(board.getTitle())                       // 게시글 제목
                .nickname(board.getWriter().getNickname())     // 작성자 닉네임
                .category(board.getCategory())                // 카테고리
                .content(board.getContent())                  // 내용
                .viewCnt(board.getViewCnt())                  // 조회수
                .createdAt(board.getCreatedAt())              // 생성일
                .updatedAt(board.getUpdatedAt())              // 수정일
                .build();
    }


    // Board를 BoardListResponseDto로 변환
    private BoardListResponseDto toDto(Board board) {
        return BoardListResponseDto.builder()
                .boardId(board.getBoardId())                      // 게시글 ID
                .nickname(board.getWriter().getNickname())        // 작성자 닉네임
                .title(board.getTitle())                          // 제목
                .category(board.getCategory())                    // 카테고리
                .viewCnt(board.getViewCnt())                      // 조회수
                .createdAt(board.getCreatedAt())                  // 생성 시간
                .updatedAt(board.getUpdatedAt())                  // 수정 시간
                .build();
    }
}