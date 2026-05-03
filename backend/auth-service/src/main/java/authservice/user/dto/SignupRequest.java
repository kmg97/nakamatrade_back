package authservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(
        @Schema(description = "사용자 아이디", example = "nakama0")
        @NotBlank(message = "아이디는 필수 입력 입니다.")
        String username,

        @Schema(description = "비밀번호 (영문, 숫자, 특수문자 포함 8~15자)", example = "!a123456")
        @NotBlank(message = "비밀번호는 필수 입력 입니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~@#$%^&+=!])(?=\\S+$).{8,15}$",
                message = "비밀번호는 영문자와 숫자, 특수문자를 1개 이상 포함한 8-15자를 입력하여야 합니다.")
        String password,

        @Schema(description = "생년월일", example = "1960-01-01")
        @NotBlank(message = "생년월일은 필수 입력 입니다.")
        String birthDay,

        @Schema(description = "성별", example = "Male")
        String gender
) {
}