package io.reflectoring.coderadar.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitLogAuthor {
  private String name;
  private String email;
  private long timestamp;
}
