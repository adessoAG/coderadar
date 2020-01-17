package io.reflectoring.coderadar.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadUserResponse {
  private long id;
  private String username;
}