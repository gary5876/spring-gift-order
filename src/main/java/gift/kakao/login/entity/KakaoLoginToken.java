package gift.kakao.login.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kakao_login_token")
public class KakaoLoginToken {

    @Id
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    protected KakaoLoginToken() {
    }

    public KakaoLoginToken(String email, String accessToken) {
        this.email = email;
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void updateToken(String accessToken) {
        this.accessToken = accessToken;
    }
}