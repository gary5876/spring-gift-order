package gift.orders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import gift.global.exception.KakaoTokenNotFoundException;
import gift.kakao.login.config.KakaoClient;
import gift.kakao.login.entity.KakaoLoginToken;
import gift.kakao.login.repository.KakaoRepository;
import gift.kakao.order.dto.OrderRequest;
import gift.kakao.order.entity.Order;
import gift.kakao.order.repository.OrderRepository;
import gift.kakao.order.service.OrderService;
import gift.member.entity.Member;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.product.entity.Product;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OptionRepository optionRepository;
    @Mock private WishRepository wishRepository;
    @Mock private KakaoRepository kakaoRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private KakaoClient kakaoClient;

    @InjectMocks
    private OrderService orderService;

    private Member member;
    private Product product;
    private Option option;
    private Wish wish;
    private OrderRequest orderRequest;
    private KakaoLoginToken kakaoToken;

    @BeforeEach
    void setUp() {
        member = new Member("test@email.com", "pw", "USER");
        product = new Product("인형", new BigDecimal("10000"), "img.jpg");
        option = new Option("기본", 10, product);
        wish = new Wish(member, product, option, 1);
        orderRequest = new OrderRequest(1L, 1L, 2, "message!");
        kakaoToken = new KakaoLoginToken(member.getEmail(), "access-token");
    }

    @Test
    @DisplayName("정상적인 주문 - 옵션 수량 차감, 위시 삭제, 카카오 메시지 전송")
    void createOrder_success() {
        // given
        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));
        when(wishRepository.findByMemberAndProduct(member, product)).thenReturn(Optional.of(wish));
        when(kakaoRepository.findById(member.getEmail())).thenReturn(Optional.of(kakaoToken));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        orderService.order(member, orderRequest);

        // then
        assertThat(option.getQuantity()).isEqualTo(8);
        verify(wishRepository).delete(wish);
        verify(kakaoClient).sendOrderMessage(eq(kakaoToken.getAccessToken()), any(Order.class));
    }

    @Test
    @DisplayName("카카오 토큰이 없으면 예외 발생")
    void createOrder_kakaoTokenNotFound() {
        // given
        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));
        when(wishRepository.findByMemberAndProduct(member, product)).thenReturn(Optional.of(wish));
        when(kakaoRepository.findById(member.getEmail())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.order(member, orderRequest))
                .isInstanceOf(KakaoTokenNotFoundException.class);
    }
}
