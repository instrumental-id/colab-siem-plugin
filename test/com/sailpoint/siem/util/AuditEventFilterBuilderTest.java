package com.sailpoint.siem.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Tests for the audit event action filter builder. These run against real
 * IdentityIQ Filter objects, so they need IdentityIQ on the classpath like the
 * rest of the test suite.
 */
public class AuditEventFilterBuilderTest {

	@Test
	public void parseListHandlesNullAndBlank() {

		assertTrue(AuditEventFilterBuilder.parseList(null).isEmpty());
		assertTrue(AuditEventFilterBuilder.parseList("").isEmpty());
		assertTrue(AuditEventFilterBuilder.parseList(" , ,").isEmpty());
	}

	@Test
	public void parseListTrimsAndDeDuplicates() {

		assertEquals(Arrays.asList("a", "b", "c*"), AuditEventFilterBuilder.parseList(" a, b ,a,,c* "));
	}

	@Test
	public void parseListIgnoresBareWildcard() {

		// A lone "*" would match every action and defeat an exclude list.
		assertTrue(AuditEventFilterBuilder.parseList("*").isEmpty());
	}

	@Test
	public void matchAnyReturnsNullWithoutValues() {

		assertNull(AuditEventFilterBuilder.matchAny("action", null));
		assertNull(AuditEventFilterBuilder.matchAny("action", Collections.<String> emptyList()));
	}

	@Test
	public void matchAnyBuildsFilterForExactAndPrefixValues() {

		assertNotNull(AuditEventFilterBuilder.matchAny("action", Arrays.asList("a")));
		assertNotNull(AuditEventFilterBuilder.matchAny("action", Arrays.asList("a", "b")));
		assertNotNull(AuditEventFilterBuilder.matchAny("action", Arrays.asList("hist*")));
		assertNotNull(AuditEventFilterBuilder.matchAny("action", Arrays.asList("a", "hist*")));
	}

	@Test
	public void includeExcludeAddsNothingWhenUnrestricted() {

		List<String> none = Collections.emptyList();
		assertTrue(AuditEventFilterBuilder.includeExclude("action", none, none).isEmpty());
	}

	@Test
	public void includeExcludeAddsOneFilterPerList() {

		List<String> some = Arrays.asList("a");
		List<String> none = Collections.emptyList();
		assertEquals(1, AuditEventFilterBuilder.includeExclude("action", some, none).size());
		assertEquals(1, AuditEventFilterBuilder.includeExclude("action", none, some).size());
		assertEquals(2, AuditEventFilterBuilder.includeExclude("action", some, some).size());
	}
}
