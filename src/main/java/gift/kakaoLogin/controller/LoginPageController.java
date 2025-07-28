package gift.kakaoLogin.controller;

import gift.kakaoLogin.config.KakaoProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/kakao")
public class LoginPageController {

    private static final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    private final KakaoProperties kakaoProperties;

    public LoginPageController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        String redirectUrl = buildKakaoAuthorizationUrl();
        model.addAttribute("location", redirectUrl);
        return "kakaoLogin";
    }

    private String buildKakaoAuthorizationUrl() {
        return UriComponentsBuilder
                .fromHttpUrl(KAKAO_AUTH_URL)
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .build()
                .toUriString();
    }
}
