package org.wickedsource.coderadar.projectadministration.port.driver.module;

import lombok.Value;

@Value
public class UpdateModuleCommand {
  private Long id;
  private String path;
}
