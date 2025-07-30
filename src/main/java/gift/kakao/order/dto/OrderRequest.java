package gift.kakao.order.dto;

public record OrderRequest(
        Long optionId,
        int quantity,
        String message
) {}
