package com.ssafy.undaid.domain.board.entity.repository;


import com.ssafy.undaid.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    //조회수 증가를 위한 메서드
    @Modifying
    @Query("UPDATE Board b SET b.viewCnt = b.viewCnt + 1 WHERE b.boardId = :boardId")
    void incrementViewCount(@Param("boardId") Integer boardId);
}