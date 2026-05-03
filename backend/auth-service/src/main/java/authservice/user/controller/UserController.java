package authservice.user.controller;

import authservice.global.exception.ErrorCode;
import authservice.global.swagger.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import authservice.global.common.Result;
import authservice.user.dto.SignupRequest;
import authservice.user.dto.SignupResponse;
import authservice.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "사용자 관리", description = "사용자 관련 도메인 서비스")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String SIGNUP_SUCCESS_MESSAGE = "회원가입이 완료되었습니다.";

    private final UserService userService;

    @Operation(summary = "유저 회원가입")
    @ApiResponse(
            responseCode = "200",
            description = "회원가입 성공시 응답메시지와 로그인 정보가 반환된다."
    )
    @ApiErrorResponse(value = {
            ErrorCode.INVALID_INPUT_VALUE,
            ErrorCode.INVALID_PASSWORD,
            ErrorCode.ROLE_NOT_FOUND,
            ErrorCode.DUPLICATE_USERNAME
    })
    @PostMapping(value = "/register", produces = CONTENT_TYPE_JSON)
    public ResponseEntity<Result<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse signupResponse = userService.signup(request);

        return ResponseEntity.ok(Result.success(SIGNUP_SUCCESS_MESSAGE, signupResponse));
    }

    @Operation(summary = "유저 중복 확인")
    @ApiResponse(
            responseCode = "200",
            description = "요청 파라미터로 전달된 username을 사용하는 사용자의 존재 여부를 Boolean 값으로 반환된다."
    )
    @GetMapping(value = "/exists", produces = CONTENT_TYPE_JSON)
    public ResponseEntity<Result<Boolean>> usernameExists(
            @Parameter(description = "확인할 사용자 username", example = "nakama0")
            @RequestParam String username) {
        Boolean existsChecked = Boolean.valueOf(userService.usernameExistsCheck(username));

        return ResponseEntity.ok(Result.success(existsChecked));
    }
}
