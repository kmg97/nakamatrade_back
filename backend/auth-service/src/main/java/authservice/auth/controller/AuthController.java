package authservice.auth.controller;

import authservice.global.exception.ErrorCode;
import authservice.global.swagger.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import authservice.auth.dto.LoginRequest;
import authservice.auth.dto.TokenDto;
import authservice.auth.service.AuthService;
import authservice.global.common.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증/인가 관리", description = "인증 및 인가 관련 도메인 서비스")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String CONTENT_TYPE_JSON = "application/json";

    private final AuthService authService;

    @Operation(summary = "회원 로그인")
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공시 응답메시지와 JWT가 전달된다."
    )
    @ApiErrorResponse(value = {
            ErrorCode.ACCOUNT_LOCKED,
            ErrorCode.USER_NOT_FOUND
    })
    @PostMapping(value = "/login", produces = CONTENT_TYPE_JSON)
    public ResponseEntity<Result<TokenDto>> login(@Valid @RequestBody LoginRequest request) {
        TokenDto tokenDto = authService.login(request);

        return ResponseEntity.ok(Result.success(tokenDto));
    }
}