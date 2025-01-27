package com.ssafy.undaid.domain.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

    @Getter
    @Setter
    @Builder
    public class BoardDetailResponseDto {
        private String title;
        private String nickname;
        private Byte category;
        private String content;
        private Integer viewCnt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }
