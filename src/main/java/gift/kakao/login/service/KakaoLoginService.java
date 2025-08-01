package gift.kakao.login.service;

import gift.global.exception.EmailNotFoundException;
import gift.global.exception.MemberNotFoundException;
import gift.kakao.login.config.KakaoProperties;
import gift.kakao.login.dto.KakaoUserInfoResponse;
import gift.kakao.login.service.KakaoService;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.global.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class KakaoLoginService {

    private final KakaoService kakaoService;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    private static final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    private final KakaoProperties kakaoProperties;

    public KakaoLoginService(
            KakaoService kakaoService,
            MemberRepository memberRepository,
            JwtUtil jwtUtil,
            KakaoProperties kakaoProperties
    ) {
        this.kakaoService = kakaoService;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.kakaoProperties = kakaoProperties;
    }

    public String kakaoLogin(String code) {

        String accessToken = kakaoService.getAccessToken(code);

        KakaoUserInfoResponse userInfo = kakaoService.getUserInfo(accessToken);
        String email = userInfo.kakao_account().email();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

        return jwtUtil.generateToken(member);
    }

    public String loginByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));

        return jwtUtil.generateToken(member);
    }

    public String buildAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUri(URI.create(KAKAO_AUTH_URL))
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .build()
                .toUriString();
    }
}
