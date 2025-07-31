package gift.kakao.login.controller;

import gift.kakao.login.config.KakaoProperties;
import gift.kakao.login.service.KakaoLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
public class LoginViewController {

    private final KakaoProperties kakaoProperties;
    private final KakaoLoginService kakaoLoginService;

    public LoginViewController(
            KakaoProperties kakaoProperties,
            KakaoLoginService kakaoLoginService
    ) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoLoginService = kakaoLoginService;
    }


    @GetMapping("/login")
    public String loginPage(Model model) {
        String redirectUrl = kakaoLoginService.buildAuthorizationUrl();
        model.addAttribute("location", redirectUrl);
        return "kakaoLogin";
    }

    @GetMapping("/callback")
    public String callbackView() {
        return "kakaoCallback";
    }
}
