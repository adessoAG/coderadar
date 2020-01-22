package io.reflectoring.coderadar.query.port.driver;

import io.reflectoring.coderadar.query.domain.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetHistoryOfMetricCommand {
  @NotBlank private String metricName;
  private Date start;
  private Date end;
  private Interval interval;
}
