package authservice.auth.service;

import authservice.global.exception.BusinessException;
import authservice.user.domain.User;
import authservice.user.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import authservice.auth.dto.LoginRequest;
import authservice.auth.dto.TokenDto;
import authservice.global.exception.ErrorCode;
import authservice.global.security.JwtProvider;
import authservice.global.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Transactional(noRollbackFor = BusinessException.class)
    public TokenDto login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            UserPrincipal userPrinciapl = (UserPrincipal) authentication.getPrincipal();
            Long userId = userPrinciapl.getUser().getId();
            
            userService.handleLoginSuccess(request);

            return tokenIssueAndSave(authentication, userId);
        } catch (BadCredentialsException e) {
            int failCount = userService.handleLoginFailure(request);

            String errorMassage = "비밀번호가 일치하지 않습니다. (실패 횟수: " + failCount + ")";
            throw new BusinessException(ErrorCode.INVALID_PASSWORD, errorMassage);
        } catch (LockedException e) {
        	throw new BusinessException(ErrorCode.ACCOUNT_LOCKED);
        }
    }
    
    @Transactional
    public TokenDto reissue(String refreshToken) {
    	if (!jwtProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    	
    	String userId = jwtProvider.parseClaims(refreshToken).getSubject();
    	
    	refreshTokenService.validateAndCheckTokenMatch(userId, refreshToken);
    	
    	User user = userService.findByIdWithRoles(Long.valueOf(userId));
    	UserDetails userDetails = new UserPrincipal(user);
    	
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );

        return tokenIssueAndSave(authentication, Long.valueOf(userId));
    }
    
    private TokenDto tokenIssueAndSave(Authentication authentication, Long userId) {
    	TokenDto updatedToken = jwtProvider.generateToken(authentication, Long.valueOf(userId));
    	
        refreshTokenService.save(String.valueOf(userId), updatedToken.getRefreshToken(), jwtProvider.getRefreshTokenExpirationMillis());
        
        return updatedToken;
    }
}