package com.example.auth.domain.post.post.entity;

import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.post.comment.entity.Comment;
import com.example.auth.global.entity.BaseTime;
import com.example.auth.global.exception.ServiceException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post extends BaseTime {
    //Post 엔터티는 Member 엔터티와 다대일(N:1) 관계를 맺음
    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;
    private String title;
    private String content;

    //Post 엔터티는 Comment 엔터티와 일대다(1:N) 관계를 맺음
    @Builder.Default
    @OneToMany(mappedBy = "post",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();//Post 엔터티가 Comment 리스트를 포함하도록 함. 즉, 해당 Post가 삭제되면 Comment도 삭제됨.
    public Comment addComment(Member author, String content) {
        Comment comment = Comment
                .builder()
                .post(this)
                .author(author)
                .content(content)
                .build();
        comments.add(comment);
        return comment;
    }

    //id로 댓글 찾기
    public Comment getCommentById(long id) {
        return comments.stream()
                .filter(comment -> comment.getId() == id)
                .findFirst()
                .orElseThrow(
                        () -> new ServiceException("404-2", "존재하지 않는 댓글입니다.")
                );
    }
}