package org.wickedsource.coderadar.vcs.git;

import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Assert;
import org.junit.Test;
import org.wickedsource.coderadar.testframework.template.GitTestTemplate;

public class GitCommitFinderTest extends GitTestTemplate {

	@Test
	public void findExistingCommit() throws Exception {
		RevCommit commit1 = commit();
		RevCommit commit2 = commit();
		RevCommit commit3 = commit();

		GitCommitFinder finder = new GitCommitFinder();
		RevCommit foundCommit1 = finder.findCommit(git, commit1.getName());
		RevCommit foundCommit2 = finder.findCommit(git, commit2.getName());
		RevCommit foundCommit3 = finder.findCommit(git, commit3.getName());

		Assert.assertEquals(commit1.getName(), foundCommit1.getName());
		Assert.assertEquals(commit2.getName(), foundCommit2.getName());
		Assert.assertEquals(commit3.getName(), foundCommit3.getName());
	}

	@Test
	public void findNonExistingCommit() throws Exception {
		RevCommit commit1 = commit();
		RevCommit commit2 = commit();
		RevCommit commit3 = commit();

		GitCommitFinder finder = new GitCommitFinder();
		Assert.assertNull(finder.findCommit(git, "36db3431f4ecd789ba0cf8a44954ef51d7402825"));
	}
}
