package gift.kakao.login.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.kakao.login.config.KakaoProperties;
import gift.kakao.login.dto.KakaoUserInfoResponse;
import gift.kakao.login.entity.KakaoLoginToken;
import gift.kakao.login.repository.KakaoRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final RestClient restClient;
    private final KakaoProperties kakaoProperties;
    private final KakaoRepository kakaoRepository;
    private final ObjectMapper objectMapper;

    public KakaoService(KakaoProperties kakaoProperties,
                        KakaoRepository kakaoRepository,
                        ObjectMapper objectMapper) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoRepository = kakaoRepository;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().build();
    }

    public String getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);

        try {
            String response = restClient.post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(params)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            return root.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("토큰 파싱 실패", e);
        }
    }

    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        try {
            String response = restClient.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .body(String.class);

            KakaoUserInfoResponse userInfo = objectMapper.readValue(response, KakaoUserInfoResponse.class);
            String email = userInfo.kakao_account().email();

            kakaoRepository.save(new KakaoLoginToken(email, accessToken));
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("유저정보 파싱 실패", e);
        }
    }
}
