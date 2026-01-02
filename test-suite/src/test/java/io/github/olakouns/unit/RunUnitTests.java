package io.github.olakouns.unit;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("All Unit Tests")
@SelectPackages("io.github.olakouns.unit")
@IncludeTags("unit")
@ExcludeClassNamePatterns(".*RunUnitTests.*")
public class RunUnitTests {
}
