package gift.kakao.order.controller;

import gift.global.resolver.LoginMember;
import gift.member.entity.Member;
import gift.kakao.order.dto.OrderRequest;
import gift.kakao.order.dto.OrderResponse;
import gift.kakao.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> order(
            @LoginMember Member member,
            @RequestBody OrderRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");

        OrderResponse response = orderService.order(member, request, accessToken);
        return ResponseEntity.status(201).body(response);
    }
}
