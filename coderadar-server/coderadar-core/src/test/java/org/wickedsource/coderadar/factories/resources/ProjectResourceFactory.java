package org.wickedsource.coderadar.factories.resources;

import java.time.LocalDate;
import org.wickedsource.coderadar.project.rest.ProjectResource;

public class ProjectResourceFactory {

	public ProjectResource validProjectResource() {
		ProjectResource project = new ProjectResource();
		project.setVcsUser("user");
		project.setVcsPassword("pass");
		project.setVcsUrl("http://valid.url");
		project.setName("name");
		project.setStartDate(LocalDate.of(2016, 1, 1));
		project.setEndDate(LocalDate.of(2016, 12, 31));
		return project;
	}

	public ProjectResource validProjectResource2() {
		ProjectResource project = new ProjectResource();
		project.setVcsUser("user2");
		project.setVcsPassword("pass2");
		project.setVcsUrl("http://valid.url2");
		project.setName("name2");
		project.setStartDate(LocalDate.of(2016, 1, 1));
		project.setEndDate(LocalDate.of(2016, 12, 31));
		return project;
	}
}
