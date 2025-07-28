package gift.kakaoLogin.dto;

import java.util.Map;

public class KakaoUserInfoResponse {
    private Long id;
    private Map<String, String> properties;

    public KakaoUserInfoResponse(Long id) {
        this.id = id;
    }

    public KakaoUserInfoResponse(Long id, Map<String, String> properties) {
        this.id = id;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
