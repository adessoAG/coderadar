package io.reflectoring.coderadar.useradministration.port.driver.refresh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenCommand {
  @NotBlank private String accessToken;
  @NotBlank private String refreshToken;
}
