package io.github.razacki.unit.core.util;


import io.github.util.Time;
import org.junit.jupiter.api.*;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Tag("unit")
@DisplayName("Time Utility Tests")
class TimeTest {

    @BeforeEach
    void resetOffset() {
        Time.setOffset(0);
    }

    @Test
    @DisplayName("Should return current time in seconds")
    void shouldReturnCurrentTimeInSeconds() {
        // Arrange
        long expected = System.currentTimeMillis() / 1000;

        // Act
        int actual = Time.currentTime();

        // Assert
        assertThat((long) actual).isCloseTo(expected, within(1L));
    }

    @Test
    @DisplayName("Should apply offset to current time")
    void shouldApplyOffsetToCurrentTime() {
        // Arrange
        int offsetSeconds = 3600; // 1 hour
        Time.setOffset(offsetSeconds);
        long expected = (System.currentTimeMillis() / 1000) + offsetSeconds;

        // Act
        int actual = Time.currentTime();

        // Assert
        assertThat((long) actual).isCloseTo(expected, within(1L));
    }

    @Test
    @DisplayName("Should return current time in millis with offset")
    void shouldReturnCurrentTimeMillisWithOffset() {
        // Arrange
        int offsetSeconds = 10;
        Time.setOffset(offsetSeconds);
        long before = System.currentTimeMillis() + (offsetSeconds * 1000L);

        // Act
        long actual = Time.currentTimeMillis();

        // Assert
        long after = System.currentTimeMillis() + (offsetSeconds * 1000L);
        assertThat(actual).isBetween(before, after);
    }

    @Test
    @DisplayName("Should convert seconds and millis to Date objects")
    void shouldConvertToDate() {
        // Arrange
        int timeInSeconds = 1672531200; // 2023-01-01 00:00:00
        long timeInMillis = 1672531200000L;

        // Act
        Date dateFromInt = Time.toDate(timeInSeconds);
        Date dateFromLong = Time.toDate(timeInMillis);

        // Assert
        assertThat(dateFromInt.getTime()).isEqualTo(timeInMillis);
        assertThat(dateFromLong.getTime()).isEqualTo(timeInMillis);
    }

    @Test
    @DisplayName("Should convert seconds to millis correctly")
    void shouldConvertToMillis() {
        // Arrange
        long seconds = 123L;
        long expectedMillis = 123000L;

        // Act
        long actualMillis = Time.toMillis(seconds);

        // Assert
        assertThat(actualMillis).isEqualTo(expectedMillis);
    }

    @Test
    @DisplayName("Should correctly get and set offset")
    void shouldGetAndSetOffset() {
        // Act
        Time.setOffset(500);

        // Assert
        assertThat(Time.getOffset()).isEqualTo(500);
    }
}