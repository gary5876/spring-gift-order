package gift.kakao.login.dto;

public record KakaoUserInfoResponse(
        Long id,
        String connected_at,
        KakaoAccount kakao_account
) {
    public record KakaoAccount(
            boolean has_email,
            boolean email_needs_agreement,
            boolean is_email_valid,
            boolean is_email_verified,
            String email
    ) {}
}
