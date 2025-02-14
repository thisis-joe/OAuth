package com.example.auth.domain.member.member.service;

import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(String username, String password, String nickname) {

//        UUID uuid = UUID.randomUUID();  //-> 왜 지우는가? -> apiKey를 UUID로 생성하는 것은 무의미하다. 왜냐하면 UUID는 매번 새로 생성되기 때문이다.

        Member member = Member.builder()
                .username(username)
                .password(password)
                .apiKey(username) //개발 환경에서 POSTMAN에서 매번 바꾸지 않기 위해 apiKey는 username으로 설정. 실제로는 더 복잡한 방식으로 생성해야 함.
                .nickname(nickname)
                .build();

        return memberRepository.save(member);
    }
    public long count() {
        return memberRepository.count();
    }
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
    public Optional<Member> findByApiKey(String apikey) {
        return memberRepository.findByApiKey(apikey);
    }
}