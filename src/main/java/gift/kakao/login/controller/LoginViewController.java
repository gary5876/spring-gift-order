package gift.kakao.login.controller;

import gift.kakao.login.config.KakaoProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
public class LoginViewController {

    private static final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    private final KakaoProperties kakaoProperties;

    public LoginViewController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }


    @GetMapping("/login")
    public String loginPage(Model model) {
        String redirectUrl = buildKakaoAuthorizationUrl();
        model.addAttribute("location", redirectUrl);
        return "kakaoLogin";
    }

    @GetMapping("/callback")
    public String callbackView() {
        return "kakaoCallback";
    }

    private String buildKakaoAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUri(URI.create(KAKAO_AUTH_URL))
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .build()
                .toUriString();
    }
}
