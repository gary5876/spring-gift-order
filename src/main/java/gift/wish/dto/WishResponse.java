package gift.wish.dto;

import gift.product.entity.Product;
import gift.wish.entity.Wish;
import gift.option.entity.Option;

import java.math.BigDecimal;

import java.util.List;

public record WishResponse(
        Long optionId,
        Long productId,
        String name,
        BigDecimal price,
        String imgUrl,
        int quantity,
        List<OptionSummary> options  // ✅ 추가
) {
    public static WishResponse from(Wish wish, List<OptionSummary> allOptions) {
        Product product = wish.getProduct();
        Option option = wish.getOption();
        return new WishResponse(
                option.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImgUrl(),
                wish.getQuantity(),
                allOptions
        );
    }

    public record OptionSummary(Long optionId, String name, int quantity) {
        public static OptionSummary from(Option o) {
            return new OptionSummary(o.getId(), o.getName(), o.getQuantity());
        }
    }
}
