package gift.kakao.order.service;

import gift.kakao.login.config.KakaoClient;
import gift.kakao.login.entity.KakaoLoginToken;
import gift.kakao.login.repository.KakaoRepository;
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
    private final KakaoRepository kakaoRepository;

    public OrderService(
            OrderRepository orderRepository,
            OptionRepository optionRepository,
            WishRepository wishRepository,
            KakaoClient kakaoClient,
            KakaoRepository kakaoRepository
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoClient = kakaoClient;
        this.kakaoRepository = kakaoRepository;
    }

    @Transactional
    public OrderResponse order(Member member, OrderRequest request) {

        System.out.println("OS_Point0");

        Option option = optionRepository.findById(request.optionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

        System.out.println("OS_Point1");

        option.decreaseQuantity(request.quantity());

        System.out.println("OS_Point2");

        Order order = new Order(option, request.quantity(), request.message());

        System.out.println("OS_Point3");

        orderRepository.save(order);

        System.out.println("OS_Point4");

        wishRepository.deleteByMemberAndProduct(member, option.getProduct());

        System.out.println("OS_Point5");

        KakaoLoginToken token = kakaoRepository.findById(member.getEmail())
                .orElseThrow(() -> new IllegalStateException("카카오 access token이 없습니다."));

        System.out.println("OS_Point6: " + token.getAccessToken());

        kakaoClient.sendOrderMessage(token.getAccessToken(), order);

        System.out.println("OS_Point7");

        return OrderResponse.from(order);
    }
}

