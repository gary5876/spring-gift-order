package gift.wish.service;

import gift.global.exception.OptionNotFoundException;
import gift.global.exception.ProductNotFoundException;
import gift.global.exception.WishAlreadyExistsException;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.product.dto.ProductResponse;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wish.dto.WishResponse;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public WishService(WishRepository wishRepository,
                       MemberRepository memberRepository,
                       ProductRepository productRepository,
                       OptionRepository optionRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    public List<ProductResponse> getAllWishes(Member member) {
        List<Wish> wishes = wishRepository.findByMember(member);
        return wishes.stream()
                .map(Wish::getProduct)
                .map(ProductResponse::from)
                .toList();
    }

    public Page<WishResponse> getPagedWishes(Member member, Pageable pageable) {
        return wishRepository.findByMember(member, pageable)
                .map(wish -> {
                    List<WishResponse.OptionSummary> optionSummaries = wish.getProduct().getOptions().stream()
                            .map(WishResponse.OptionSummary::from)
                            .toList();
                    return WishResponse.from(wish, optionSummaries);
                });
    }

    @Transactional
    public void addWish(Member member, Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Option option;
        if (optionId != null) {
            option = optionRepository.findById(optionId)
                    .orElseThrow(() -> new OptionNotFoundException(optionId));
        } else {
            option = product.getOptions().stream()
                    .findFirst()
                    .orElseThrow(() -> new OptionNotFoundException(optionId));
        }

        if (wishRepository.existsByMemberAndProduct(member, product)) {
            throw new WishAlreadyExistsException(product);
        }

        wishRepository.save(new Wish(member, product, option, 1));
    }

    @Transactional
    public void deleteWish(Member member, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        wishRepository.deleteByMemberAndProduct(member, product);
    }

    @Transactional
    public void updateWishQuantity(Member member, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Wish wish = wishRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (quantity <= 0) {
            wishRepository.delete(wish);
        } else {
            wish.updateQuantity(quantity);
        }
    }

    @Transactional
    public void updateWishOption(Member member, Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Wish wish = wishRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException(optionId));

        wish.updateOption(option);
    }
}
