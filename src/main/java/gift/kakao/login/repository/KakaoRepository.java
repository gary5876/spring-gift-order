package gift.kakao.login.repository;

import gift.kakao.login.entity.KakaoLoginToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoRepository extends JpaRepository<KakaoLoginToken, String> {
}