package me.mrletsplay.mrcore;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import me.mrletsplay.mrcore.permission.Permission;

public class PermissionsTest {

	@Test
	public void testPermissionIncludes() {
		Permission perm = new Permission("a.permission.*");
		assertTrue(perm.includes("a.permission.sub.permission"));
	}

}
