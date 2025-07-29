package gift.kakaoLogin.login.controller;

import gift.kakaoLogin.login.dto.KakaoUserInfoResponse;
import gift.kakaoLogin.login.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class KakaoLoginCallbackController {

    private final KakaoService kakaoService;

    public KakaoLoginCallbackController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessToken(code);
        KakaoUserInfoResponse userInfo = kakaoService.getUserInfo(accessToken);

        return ResponseEntity.ok(Map.of(
                "token", accessToken,
                "userInfo", userInfo
        ));
    }

}
