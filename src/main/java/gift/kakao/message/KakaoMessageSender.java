package gift.kakao.message;

import org.springframework.stereotype.Component;

@Component
public class KakaoMessageSender {

    public void sendMessageToMe(String memberCode, String message) {
        System.out.println("[카카오 메시지 전송] code: " + memberCode + ", message: " + message);
    }
}
