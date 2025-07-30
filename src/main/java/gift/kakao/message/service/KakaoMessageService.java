package gift.kakao.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoMessageService {

    private static final String KAKAO_MESSAGE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendOrderMessageToMe(String accessToken, String productName, int quantity, String customMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        // 메시지 템플릿 구성
        Map<String, Object> templateObject = Map.of(
                "object_type", "text",
                "text", String.format(
                        "🛒 주문 완료!\n상품명: %s\n수량: %d개\n요청사항: %s",
                        productName, quantity, customMessage != null ? customMessage : "-"
                ),
                "link", Map.of(
                        "web_url", "http://localhost:8080",
                        "mobile_web_url", "http://localhost:8080"
                ),
                "button_title", "주문 내역 확인"
        );

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        try {
            body.add("template_object", objectMapper.writeValueAsString(templateObject));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 메시지 JSON 생성 실패", e);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_MESSAGE_URL, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("카카오 메시지 전송 실패: " + response.getBody());
        }
    }
}
