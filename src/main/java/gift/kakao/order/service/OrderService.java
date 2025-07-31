package gift.kakao.order.service;

import gift.global.exception.KakaoTokenNotFoundException;
import gift.global.exception.OptionNotFoundException;
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
        Option option = optionRepository.findById(request.optionId())
                .orElseThrow(() -> new OptionNotFoundException(request.optionId()));
        option.decreaseQuantity(request.quantity());

        Order order = new Order(option, request.quantity(), request.message());
        orderRepository.save(order);

        wishRepository.deleteByMemberAndProduct(member, option.getProduct());

        KakaoLoginToken token = kakaoRepository.findById(member.getEmail())
                .orElseThrow(() -> new KakaoTokenNotFoundException(member.getEmail()));
        kakaoClient.sendOrderMessage(token.getAccessToken(), order);

        return OrderResponse.from(order);
    }
}

