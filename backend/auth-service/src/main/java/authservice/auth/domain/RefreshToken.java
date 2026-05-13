package authservice.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Builder;
import lombok.Getter;

@RedisHash(value = "RT")
@Getter
public class RefreshToken {
	@Id
	private String userId;
	
	private String token;
	
	@TimeToLive
	private Long expiration;
	
	@Builder
	private RefreshToken (String userId, String token, Long expiration) {
		this.userId = userId;
		this.token = token;
		this.expiration = expiration;
	}
}
