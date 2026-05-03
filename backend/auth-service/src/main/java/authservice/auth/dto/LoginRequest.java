package authservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "사용자 아이디", example = "nakama0")
        @NotBlank(message = "아이디를 입력해주세요.")
        String username,

        @Schema(description = "비밀번호 (영문, 숫자, 특수문자 포함 8~15자)", example = "!a123456")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
}
