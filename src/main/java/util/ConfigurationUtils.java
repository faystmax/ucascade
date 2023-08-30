package util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.gitlab4j.api.models.Branch;

public class ConfigurationUtils {
	public static final String LAST_BRANCH = "master";
	public static final Pattern BRANCH_PATTER = Pattern.compile("release/release-(\\d+\\.\\d+)");

	public static String getNextTargetBranch(List<Branch> branches, String sourceBranchName) {
		if (BRANCH_PATTER.matcher(sourceBranchName).find()) {
			final List<Branch> sortedBranches = branches.stream()
					.filter(Objects::nonNull)
					.map(util.ComparableBranch::new)
					.filter(branch -> branch.getVersion() != null)
					.sorted(Comparator.comparing(util.ComparableBranch::getVersion))
					.map(util.ComparableBranch::getBranch)
					.toList();

			for (Iterator<Branch> iterator = sortedBranches.iterator(); iterator.hasNext();) {
				Branch currentBranch = iterator.next();
				if (Objects.equals(currentBranch.getName(), sourceBranchName) && iterator.hasNext()) {
					return iterator.next().getName();
				}
			}
		}
		return Objects.equals(sourceBranchName, LAST_BRANCH) ? null : LAST_BRANCH;
	}
}
