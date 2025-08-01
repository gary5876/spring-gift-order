package gift.global.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String email) {
        super("등록되지 않은 이메일입니다 :"+email);
    }
}
