package com.example.auth.domain.post.post.controller;


import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.member.member.service.MemberService;
import com.example.auth.domain.post.post.dto.PostDto;
import com.example.auth.domain.post.post.entity.Post;
import com.example.auth.domain.post.post.service.PostService;
import com.example.auth.global.dto.RsData;
import com.example.auth.global.exception.ServiceException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping
    public RsData<List<PostDto>> getItems() { //여러 건 조회
        List<Post> posts = postService.getItems();
        List<PostDto> postDtos = posts.stream()
                .map(PostDto::new)
                .toList();
        return new RsData<>("200-1", "글 목록 조회가 완료되었습니다.", postDtos);
    }

    @GetMapping("{id}")
    public RsData<PostDto> getItem(@PathVariable long id) { //단 건 조회
        Post post = postService.getItem(id).get();

        return new RsData<>("200-1", "글 조회가 완료되었습니다.", new PostDto(post));
    }

    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable long id) {
        Post post = postService.getItem(id).get();
        postService.delete(post);

        return new RsData<>("204-1", "%d번 글 삭제가 완료되었습니다.".formatted(id));
    }

    record ModifyReqBody(@NotBlank @Length(min = 3) String title,
                         @NotBlank @Length(min = 3) String content,
                         @NotNull Long authorId,
                         @NotBlank @Length(min = 3) String password){}

    @PutMapping("{id}")
    public RsData<Void> modify(@PathVariable long id, @RequestBody @Valid ModifyReqBody body) {

        Member actor = memberService.findById(body.authorId()).get();

        if (!actor.getPassword().equals(body.password())) {
            throw new ServiceException("401-1", "비밀번호가 일치하지 않습니다.");
        }

        Post post = postService.getItem(id).get();

        if(post.getAuthor().getId() != body.authorId()) { //작성자 확인
            throw new ServiceException("403-1", "자신이 작성한 글만 수정 가능합니다.");
        }
        postService.modify(post, body.title(), body.title());

        return new RsData<>("200-2", "%d번 글 수정이 완료되었습니다.".formatted(id), null);
    }

    record WriteReqBody(@NotBlank @Length(min = 3) String title,
                        @NotBlank @Length(min = 3) String content,
                        @NotNull Long authorId,
                        @NotBlank @Length(min = 3) String password){}

    record WriteResBody(long id, long totalCount) {
    }

    @PostMapping
    public RsData<PostDto> write(@RequestBody @Valid WriteReqBody body) {

        Member actor = memberService.findById(body.authorId()).get();
//        if(actor == null) {
//            throw new ServiceException("404-1", "사용자를 찾을 수 없습니다.");
//        }
        if(!actor.getPassword().equals(body.password())) {
            throw new ServiceException("401-1", "비밀번호가 일치하지 않습니다.");
        }
        Post post = postService.write(actor, body.title(), body.content());

        return new RsData<>(
                "200-1",
                "글 작성이 완료되었습니다.",
                new PostDto(post)
        );
    }
}