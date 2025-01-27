package com.ssafy.undaid.domain.board.dto.response;

import com.ssafy.undaid.global.common.response.HttpStatusCode;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardCreateResponseDto {
    private Integer boardId;
    private String title;
    private String content;
    private Byte category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
