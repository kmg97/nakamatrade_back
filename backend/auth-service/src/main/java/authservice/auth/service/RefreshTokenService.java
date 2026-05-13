package authservice.auth.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import authservice.auth.domain.RefreshToken;
import authservice.auth.repository.RefreshTokenRepository;
import authservice.global.exception.BusinessException;
import authservice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void save(String userId, String refreshToken, long expiredTime) {
		RefreshToken newRefreshToken = RefreshToken.builder()
				.userId(userId)
				.token(refreshToken)
				.expiration(expiredTime)
				.build();
		
		refreshTokenRepository.save(newRefreshToken);
	}
	
	@Transactional
	public void deleteByUserId(String userId) {
		refreshTokenRepository.deleteById(userId);
	}
	
	@Transactional(readOnly = true)
	public RefreshToken findByUserId(String userId) {
		return refreshTokenRepository.findById(userId)
				.orElseThrow(()->new BusinessException(ErrorCode.TOKEN_NOT_FOUND));
	}
	
	@Transactional(noRollbackFor = BusinessException.class)
	public void validateAndCheckTokenMatch(String userId, String refreshToken) {
    	RefreshToken findedRefreshToken = findByUserId(userId);
    	
    	String savedRefreshToken = findedRefreshToken.getToken();
    	
    	if(Objects.isNull(savedRefreshToken) || !savedRefreshToken.equals(refreshToken)) {
    		deleteByUserId(userId);
    		
    		throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
    	}
	}
}
