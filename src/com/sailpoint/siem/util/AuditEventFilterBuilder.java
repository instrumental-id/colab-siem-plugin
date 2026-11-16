package com.sailpoint.siem.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import sailpoint.object.Filter;

/**
 * Builds IdentityIQ query filters that restrict which audit events the
 * audit-events endpoint returns.
 *
 * Values are comma separated lists. An entry ending in "*" matches any value
 * that starts with the text before the star (for example "history*"); any other
 * entry must match exactly.
 */
public final class AuditEventFilterBuilder {

	private static final String	LIST_SEPARATOR	= ",";
	private static final String	WILDCARD		= "*";

	private AuditEventFilterBuilder() {}

	/**
	 * Split a comma separated list into trimmed, de-duplicated, non-empty entries.
	 *
	 * @param csv
	 *            The comma separated text, may be null.
	 * @return The entries in their original order, never null.
	 */
	public static List<String> parseList(String csv) {

		Set<String> values = new LinkedHashSet<String>();
		if (csv != null) {

			for (String token : csv.split(LIST_SEPARATOR)) {

				String value = token.trim();
				if (!value.isEmpty() && !WILDCARD.equals(value)) {

					values.add(value);
				}
			}
		}
		return new ArrayList<String>(values);
	}

	/**
	 * Build a filter that matches any of the given values on a property.
	 *
	 * @return The filter, or null when there are no values.
	 */
	public static Filter matchAny(String property, List<String> values) {

		if (values == null || values.isEmpty()) {

			return null;
		}

		List<String> exact = new ArrayList<String>();
		List<Filter> alternatives = new ArrayList<Filter>();
		for (String value : values) {

			if (value.endsWith(WILDCARD)) {

				alternatives.add(Filter.like(property, value.substring(0, value.length() - 1), Filter.MatchMode.START));
			} else {

				exact.add(value);
			}
		}

		if (!exact.isEmpty()) {

			alternatives.add(0, exact.size() == 1 ? Filter.eq(property, exact.get(0)) : Filter.in(property, exact));
		}
		return alternatives.size() == 1 ? alternatives.get(0) : Filter.or(alternatives);
	}

	/**
	 * Build the filters that restrict a property to an allow list and away from a
	 * deny list. Rows with no value for the property are kept when only a deny
	 * list is given, because they cannot be one of the denied values.
	 *
	 * @param property
	 *            The property to filter, such as "action".
	 * @param include
	 *            Values to keep. Empty or null means keep everything.
	 * @param exclude
	 *            Values to drop. Empty or null means drop nothing.
	 * @return The filters to AND into the query, empty when nothing is restricted.
	 */
	public static List<Filter> includeExclude(String property, List<String> include, List<String> exclude) {

		List<Filter> filters = new ArrayList<Filter>();

		Filter included = matchAny(property, include);
		if (included != null) {

			filters.add(included);
		}

		Filter excluded = matchAny(property, exclude);
		if (excluded != null) {

			filters.add(Filter.or(Filter.isnull(property), Filter.not(excluded)));
		}
		return filters;
	}
}
