package gift.global.exception;

public class OptionNotFoundException extends RuntimeException {
  public OptionNotFoundException(Long OptionId) {
    super("존재하지 않는 옵션입니다 :" + OptionId);
  }

  public OptionNotFoundException(String message) {
    super(message);
  }
}
