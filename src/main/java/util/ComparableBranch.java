package util;

import static util.ConfigurationUtils.BRANCH_PATTER;

import java.util.regex.Matcher;

import org.gitlab4j.api.models.Branch;

import com.vdurmont.semver4j.Semver;

public class ComparableBranch {
	private final Branch branch;
	private final Semver version;

	public ComparableBranch(Branch branch) {
		this.branch = branch;
		final Matcher matcher = BRANCH_PATTER.matcher(branch.getName());
		this.version = matcher.find() ? new Semver(matcher.group(1), Semver.SemverType.LOOSE) : null;
	}

	public Branch getBranch() {
		return branch;
	}

	public Semver getVersion() {
		return version;
	}
}
