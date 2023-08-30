package com.unblu.ucascade.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;

import org.gitlab4j.api.models.Branch;
import org.junit.jupiter.api.Test;

import com.unblu.ucascade.util.GitlabMockUtil.GitlabAction;

import util.ConfigurationUtils;

class GitlabMockUtilTest {

	@Test
	void gitLabMockUtilTest() {
		Map<String, Object> customProperties = Map.of(
				"detailed_merge_status", "\"broken_status\"",
				"project_id", 1234);
		String body = GitlabMockUtil.get(GitlabAction.CREATE_MR, customProperties);

		assertTrue(body.contains("\"detailed_merge_status\" : \"broken_status\","));
		assertFalse(body.contains("\"detailed_merge_status\" : \"mergeable\","));
		assertTrue(body.contains("\"project_id\" : 1234,"));
		assertFalse(body.contains("\"project_id\" : 1,"));
	}

	@Test
	void testGetNextTargetBranch1() {
		ArrayList<Branch> branches = new ArrayList<>();
		branches.add(createBranchWithName("release/release-1.0"));
		branches.add(createBranchWithName("release/release-2.0"));
		branches.add(createBranchWithName("release/release-1.1"));
		branches.add(createBranchWithName("release/release-1.2"));
		branches.add(createBranchWithName("release/release-3.2"));
		branches.add(createBranchWithName("release/release-3.0"));
		String nextBranch = ConfigurationUtils.getNextTargetBranch(branches, "release/release-1.2");
		assertEquals("release/release-2.0", nextBranch);
	}

	@Test
	void testGetNextTargetBranch2() {
		ArrayList<Branch> branches = new ArrayList<>();
		branches.add(createBranchWithName("release/release-1.0"));
		branches.add(createBranchWithName("release/release-2.0"));
		branches.add(createBranchWithName("release/release-1.1"));
		branches.add(createBranchWithName("release/release-1.2"));
		branches.add(createBranchWithName("release/release-3.2"));
		branches.add(createBranchWithName("release/release-3.0"));
		String nextBranch = ConfigurationUtils.getNextTargetBranch(branches, "release/release-1.0");
		assertEquals("release/release-1.1", nextBranch);
	}

	@Test
	void testGetNextTargetBranch3() {
		ArrayList<Branch> branches = new ArrayList<>();
		branches.add(createBranchWithName("incorrectName"));
		branches.add(createBranchWithName("release/release-2.0"));
		branches.add(createBranchWithName("release/release-1.1"));
		branches.add(createBranchWithName("release/release-1.2"));
		branches.add(createBranchWithName("release/release-3.2"));
		branches.add(createBranchWithName("release/release-3.0"));
		String nextBranch = ConfigurationUtils.getNextTargetBranch(branches, "release/release-3.2");
		assertEquals("master", nextBranch);
	}

	@Test
	void testGetNextTargetBranch4() {
		ArrayList<Branch> branches = new ArrayList<>();
		branches.add(createBranchWithName("incorrectName"));
		branches.add(createBranchWithName("release/release-2.0"));
		branches.add(createBranchWithName("release/release-1.1"));
		branches.add(createBranchWithName("release/release-1.2"));
		branches.add(createBranchWithName("release/release-3.2"));
		branches.add(createBranchWithName("release/release-3.0"));
		String nextBranch = ConfigurationUtils.getNextTargetBranch(branches, "master");
		assertNull(nextBranch);
	}

	private Branch createBranchWithName(final String name) {
		final Branch branch = new Branch();
		branch.setName(name);
		return branch;
	}
}
