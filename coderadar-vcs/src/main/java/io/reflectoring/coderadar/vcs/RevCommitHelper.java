package io.reflectoring.coderadar.vcs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;

public class RevCommitHelper {

  private RevCommitHelper() {}

  public static List<RevCommit> getRevCommits(String repositoryRoot) {
    try (Git git = Git.open(new File(repositoryRoot))) {
      List<RevCommit> revCommits = new ArrayList<>();
      git.log().all().call().forEach(revCommits::add);
      Collections.reverse(revCommits);
      return revCommits;
    } catch (NoHeadException e) {
      return new ArrayList<>();
    } catch (Exception e) {
      throw new IllegalStateException(
          String.format("Error accessing git repository at %s", repositoryRoot), e);
    }
  }
}
