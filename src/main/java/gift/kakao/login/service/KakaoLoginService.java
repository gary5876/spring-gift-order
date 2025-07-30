package gift.kakao.login.service;

import gift.kakao.login.dto.KakaoUserInfoResponse;
import gift.kakao.login.service.KakaoService;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.global.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class KakaoLoginService {

    private final KakaoService kakaoService;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public KakaoLoginService(KakaoService kakaoService, MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.kakaoService = kakaoService;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public String kakaoLogin(String code) {

        String accessToken = kakaoService.getAccessToken(code);

        KakaoUserInfoResponse userInfo = kakaoService.getUserInfo(accessToken);
        String email = userInfo.kakao_account().email();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 이메일입니다: " + email));

        return jwtUtil.generateToken(member);
    }

    public String loginByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 회원이 없습니다: " + email));

        return jwtUtil.generateToken(member);
    }
}
