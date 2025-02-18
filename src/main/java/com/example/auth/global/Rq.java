package com.example.auth.global;
import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.member.member.service.MemberService;
import com.example.auth.global.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import java.util.Optional;
// Request, Response, Session, Cookie, Header
@Component
@RequiredArgsConstructor
@RequestScope //요청이 들어올 때마다 새로운 객체 생성 -> 각 요청마다 새로운 R객체 사용되므로, 멤버변수로 request를 가지고 있어도 문제 없음
public class Rq { //인증로직 통합 클래스
    private final HttpServletRequest request;
    private final MemberService memberService;
    public Member getAuthenticatedActor() { //Api 키 없으면 401 에러, 있으면 해당 멤버 반환
        String authorizationValue = request.getHeader("Authorization");
        String apiKey = authorizationValue.substring("Bearer ".length());
        Optional<Member> opActor = memberService.findByApiKey(apiKey);
        if(opActor.isEmpty()) {
            throw new ServiceException("401-1", "잘못된 비밀번호 입니다.");
        }
        return opActor.get();
    }
}