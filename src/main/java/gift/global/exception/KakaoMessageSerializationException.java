package gift.global.exception;

public class KakaoMessageSerializationException extends RuntimeException {
    public KakaoMessageSerializationException(Throwable cause) {
        super("카카오 메시지 템플릿 JSON 직렬화 실패", cause);
    }
}
