package gift.kakao.order.dto;

public record OrderRequest(
        Long productId,
        Long optionId,
        int quantity,
        String message
) {}
