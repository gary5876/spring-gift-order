package gift.global.exception;

public class KakaoTokenNotFoundException extends RuntimeException {
    public KakaoTokenNotFoundException(String email) {
        super(email+"의 kakao access token이 없습니다.");
    }
}
