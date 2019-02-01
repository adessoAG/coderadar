package org.wickedsource.coderadar.commit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.junit.Test;

public class DateCoordinatesTest {

	@Test
	public void lastWeekOfPreviousYearDE() {
		DateCoordinates coordinates = new DateCoordinates();

		coordinates.updateFromDate(date(2016, 1, 1), Locale.GERMAN);
		assertThat(coordinates.getDayOfMonth()).isEqualTo(1);
		assertThat(coordinates.getMonth()).isEqualTo(1);
		assertThat(coordinates.getWeekOfYear()).isEqualTo(53);
		assertThat(coordinates.getYear()).isEqualTo(2016);
		assertThat(coordinates.getYearOfWeek()).isEqualTo(2015);

		coordinates.updateFromDate(date(2015, 12, 30), Locale.GERMAN);
		assertThat(coordinates.getDayOfMonth()).isEqualTo(30);
		assertThat(coordinates.getMonth()).isEqualTo(12);
		assertThat(coordinates.getWeekOfYear()).isEqualTo(53);
		assertThat(coordinates.getYear()).isEqualTo(2015);
		assertThat(coordinates.getYearOfWeek()).isEqualTo(2015);
	}

	@Test
	public void firstWeekOfCurrentYearDE() {
		DateCoordinates coordinates = new DateCoordinates();
		coordinates.updateFromDate(date(2016, 1, 4), Locale.GERMAN);
		assertThat(coordinates.getDayOfMonth()).isEqualTo(4);
		assertThat(coordinates.getMonth()).isEqualTo(1);
		assertThat(coordinates.getWeekOfYear()).isEqualTo(1);
		assertThat(coordinates.getYear()).isEqualTo(2016);
		assertThat(coordinates.getYearOfWeek()).isEqualTo(2016);
	}

	private Date date(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		return c.getTime();
	}
}
