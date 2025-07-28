package gift.kakaoLogin.controller;

import gift.kakaoLogin.dto.KakaoUserInfoResponse;
import gift.kakaoLogin.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class KakaoLoginCallbackController {

    private final KakaoService kakaoService;

    public KakaoLoginCallbackController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessToken(code);
        KakaoUserInfoResponse userInfo = kakaoService.getUserInfo(accessToken);

        System.out.println("accessToken = " + accessToken);
        System.out.println("Kakao ID = " + userInfo.getId());
        System.out.println("닉네임 = " + (userInfo.getProperties() != null ? userInfo.getProperties().get("nickname") : "N/A"));

        return "redirect:/admin/products";
    }

}
