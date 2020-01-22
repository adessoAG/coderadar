package io.reflectoring.coderadar.analyzer.loc.profiles;

import io.reflectoring.coderadar.analyzer.loc.Loc;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaLocProfileTest extends AbstractLocProfileTest {

  private LocProfile profile = new JavaLocProfile();

  @Test
  public void standard() throws IOException {
    Loc loc = countLines("/testcode/java/standard.java.loctest", profile);
    assertThat(loc.getLoc()).isEqualTo(7);
    assertThat(loc.getSloc()).isEqualTo(3);
    assertThat(loc.getEloc()).isEqualTo(2);
    assertThat(loc.getCloc()).isEqualTo(4);
  }

  @Test
  public void nested() throws IOException {
    Loc loc = countLines("/testcode/java/nested.java.loctest", profile);
    assertThat(loc.getLoc()).isEqualTo(12);
    assertThat(loc.getSloc()).isEqualTo(5);
    assertThat(loc.getEloc()).isEqualTo(3);
    assertThat(loc.getCloc()).isEqualTo(5);
  }
}
