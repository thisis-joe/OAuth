package com.example.auth.global;

import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.member.member.service.MemberService;
import com.example.auth.domain.post.post.entity.Post;
import com.example.auth.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final PostService postService;
    private final MemberService memberService;

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            self.memberInit();
            self.postInit();
        };
    }

    @Transactional
    public void memberInit() {
        if(memberService.count() > 0) {
            return;
        }
        // 회원 샘플데이터 생성
        memberService.join("system", "system1234", "시스템");
        memberService.join("admin", "admin1234", "관리자");
        memberService.join("user1", "user11234", "유저1");
        memberService.join("user2", "user21234", "유저2");
        memberService.join("user3", "user31234", "유저3");
//        memberService.join("user4", "user41234", "유저4");
//        memberService.join("user5", "user51234", "유저5");
//        memberService.join("user6", "user61234", "유저6");
//        memberService.join("user7", "user71234", "유저7");
//        memberService.join("user8", "user81234", "유저8");
//        memberService.join("user9", "user91234", "유저9");
//        memberService.join("user10", "user101234", "유저10");

    }

    @Transactional
    public void postInit() {
        // 게시글 샘플데이터 존재 시 리턴
        if(postService.count() > 0) {
            return;
        }

        //user1, user2 멤버 객체 생성
        Member user1 = memberService.findByUsername("user1").get();
        Member user2 = memberService.findByUsername("user2").get();

        // 게시글 샘플데이터 생성1
        Post post1 = postService.write(user1, "축구 하실분 모집합니다.", "저녁 6시까지 모여주세요.");
        post1.addComment(user1, "저 참석하겠습니다.");
        post1.addComment(user2, "공격수 자리 있나요?");

        // 게시글 샘플데이터 생성2
        Post post2 = postService.write(user1, "농구하실분?", "3명 모집");
        post2.addComment(user1, "저는 이미 축구하기로 함..");

        // 게시글 샘플데이터 생성3
        postService.write(user2, "title3", "content3");
    }



}