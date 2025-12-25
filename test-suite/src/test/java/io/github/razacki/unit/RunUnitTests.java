package io.github.razacki.unit;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("All Unit Tests")
@SelectPackages("io.github.razacki.unit")
@IncludeTags("unit")
@ExcludeClassNamePatterns(".*RunUnitTests.*")
public class RunUnitTests {
}
