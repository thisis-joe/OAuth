package com.example.auth.domain.post.post.dto;

import com.example.auth.domain.post.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class PostDto {
    private long id;
    @JsonProperty("createdDatetime")
    private LocalDateTime createdDate;
    @JsonProperty("modifiedDatetime")
    private LocalDateTime modifiedDate;
    private String title;
    private String content;
    private long authorId; // 기본 타입(long) → Validation 적용 불가 (null이면 자동으로 기본값 0으로 설정됨 → 올바른 예외 처리가 되지 않음)
    //@NotNull
    //private Long authorId; // 객체 타입(Long) → Null 체크 가능
    private String authorName;

    public PostDto(Post post) {
        this.id = post.getId();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = post.getAuthor().getId();
        this.authorName = post.getAuthor().getNickname();
    }

}