package gift.kakaoLogin.login.dto;

public class KakaoTokenResponse {
    private String access_token;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }
}
