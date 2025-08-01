package gift.kakao.login.controller;

import gift.kakao.login.dto.KakaoUserInfoResponse;
import gift.kakao.login.service.KakaoLoginService;
import gift.kakao.login.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/kakao")
public class LoginApiController {

    private final KakaoService kakaoService;
    private final KakaoLoginService kakaoLoginService;

    public LoginApiController(KakaoService kakaoService, KakaoLoginService kakaoLoginService) {
        this.kakaoService = kakaoService;
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {

        String kakaoAccessToken = kakaoService.getAccessToken(code);
        KakaoUserInfoResponse userInfo = kakaoService.getUserInfo(kakaoAccessToken);

        String email = userInfo.kakao_account().email();
        String jwt = kakaoLoginService.loginByEmail(email);

        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "email", email
        ));
    }
}