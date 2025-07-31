package gift.global.exception;

public class KakaoMessageSendException extends RuntimeException {
  public KakaoMessageSendException(Throwable cause) {
    super("카카오 메시지 전송 실패", cause);
  }
}