package gift.kakao.order.service;

import gift.kakao.login.config.KakaoClient;
import gift.member.entity.Member;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.kakao.order.dto.OrderRequest;
import gift.kakao.order.dto.OrderResponse;
import gift.kakao.order.entity.Order;
import gift.kakao.order.repository.OrderRepository;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final KakaoClient kakaoClient;

    public OrderService(
            OrderRepository orderRepository,
            OptionRepository optionRepository,
            WishRepository wishRepository,
            KakaoClient kakaoClient
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoClient = kakaoClient;
    }

    @Transactional
    public OrderResponse order(Member member, OrderRequest request, String accessToken) {
        Option option = optionRepository.findById(request.optionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

        option.decreaseQuantity(request.quantity());

        Order order = new Order(option, request.quantity(), request.message());
        orderRepository.save(order);

        wishRepository.deleteByMemberAndProduct(member, option.getProduct());

        kakaoClient.sendOrderMessage(accessToken, order);

        return OrderResponse.from(order);
    }
}
