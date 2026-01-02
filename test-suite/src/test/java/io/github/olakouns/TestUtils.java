package io.github.olakouns;

import java.io.InputStream;
import java.util.Scanner;

public abstract class TestUtils {
    public static String loadJson(String path) {
        try (InputStream is = TestUtils.class.getResourceAsStream(path)) {
            assert is != null;
            try (Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A")) {

                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot load JSON from " + path, e);
        }
    }
}
