package authservice.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import authservice.auth.domain.RefreshToken;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String>{
	
}
