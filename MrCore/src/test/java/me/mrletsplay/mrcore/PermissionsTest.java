package me.mrletsplay.mrcore;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import me.mrletsplay.mrcore.permission.Permission;

public class PermissionsTest {

	@Test
	public void testPermissionIncludes() {
		Permission perm = new Permission("a.permission.*");
		assertTrue(perm.includes("a.permission.sub.permission"));
	}

}
