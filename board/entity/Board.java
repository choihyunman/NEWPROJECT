package com.ssafy.undaid.domain.board.entity;

import com.ssafy.undaid.domain.user.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer boardId;

    @NotNull
    @Column(name = "title", length = 100)
    private String title;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)  // 지연 로딩
    @JoinColumn(name = "writer_id")
    private Users writer;

    @NotNull
    @Column(name = "category")
    private Byte category;

    @NotNull
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "view_cnt")
    private Integer viewCnt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.viewCnt = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    //게시글 작성에 사용하려고 하는데, 값이 제대로 들어갈지 의문
    @Builder
    public Board(String title, String content, Byte category, Users writer) {
        this.title = title;
        this.content = content;
        this.category= category;
        this.writer = writer;
        this.createdAt = LocalDateTime.now();
        this.updatedAt= LocalDateTime.now();
        this.viewCnt = 0;
    }

    public void update(String title, String content) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
    }

}