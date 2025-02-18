package com.example.auth.domain.post.post.entity;

import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.post.comment.entity.Comment;
import com.example.auth.global.entity.BaseTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;
    private String title;
    private String content;


    @Builder.Default
    @OneToMany(mappedBy = "post",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();//Post 엔터티가 Comment 리스트를 포함하도록 함. 즉, 해당 Post가 삭제되면 Comment도 삭제됨.
    public void addComment(Member author, String content) {
        Comment comment = Comment
                .builder()
                .post(this)
                .author(author)
                .content(content)
                .build();
        comments.add(comment);
    }
}