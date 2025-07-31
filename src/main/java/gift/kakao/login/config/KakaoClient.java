package gift.kakao.login.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.exception.KakaoMessageSendException;
import gift.global.exception.KakaoMessageSerializationException;
import gift.kakao.order.entity.Order;
import gift.option.entity.Option;
import gift.product.entity.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class KakaoClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public KakaoClient(
            ObjectMapper objectMapper,
            @Value("${kakao.api.base-url:https://kapi.kakao.com}") String apiBaseUrl
    ) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .baseUrl(apiBaseUrl)
                .build();
    }

    public void sendOrderMessage(String kakaoAccessToken, Order order) {

        try{

        System.out.println("KC-SOM_Point0");

        String messageText = buildMessageText(order);

        System.out.println("KC-SOM_Point1");

        String templateJson;

        System.out.println("KC-SOM_Point2");

        try {

            System.out.println("KC-SOM_Point3");

            templateJson = objectMapper.writeValueAsString(
                    buildMessageTemplate(messageText)
            );
        } catch (Exception e) {

            System.out.println("KC-SOM_Point4");

            throw new KakaoMessageSerializationException(e);
        }

        System.out.println("KC-SOM_Point5");

        var body = new LinkedMultiValueMap<String, String>();

        System.out.println("KC-SOM_Point6");

        body.add("template_object", templateJson);

        System.out.println("KC-SOM_Point7");

        System.out.println("kakaoAccessToken = " + kakaoAccessToken);

        restClient.post()
                .uri("/v2/api/talk/memo/default/send")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        System.out.println("KC-SOM_Point8: 메시지 전송 성공");
    } catch (Exception e) {
        System.out.println("KC-SOM_Point9 메시지 전송 실패");
        e.printStackTrace();
        throw new KakaoMessageSendException(e);
    }}

    private String buildMessageText(Order order) {
        Option option = order.getOption();
        Product product = option.getProduct();

        return String.format(
                "🛒 주문 완료!\n상품명: %s\n옵션: %s\n수량: %d개\n요청사항: %s",
                product.getName(),
                option.getName(),
                order.getQuantity(),
                Optional.ofNullable(order.getMessage()).orElse("-")
        );
    }

    private Object buildMessageTemplate(String text) {
        return java.util.Map.of(
                "object_type", "text",
                "text", text,
                "link", java.util.Map.of(
                        "web_url", "http://localhost:8080",
                        "mobile_web_url", "http://localhost:8080"
                ),
                "button_title", "주문 확인"
        );
    }
}
