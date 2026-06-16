package com.scale.global.insurance.app.engine.impl;

import com.scale.global.insurance.app.engine.DatesCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatesCalculatorImplTest {

    private final DatesCalculator datesCalculator = new DatesCalculatorImpl();
    private static final LocalDate BASE_DATE = LocalDate.of(2019, 9, 2);

    static Stream<Arguments> inceptionCases() {
        return Stream.of(
                Arguments.of(LocalDate.of(2010, 1, 1), LocalDate.of(2019, 9, 2), 9),
                Arguments.of(LocalDate.of(2018, 12, 31), LocalDate.of(2019, 12, 1), 1),
                Arguments.of(LocalDate.of(2018, 12, 31), LocalDate.of(2019, 11, 30), 0),
                Arguments.of(LocalDate.of(2019, 9, 1), LocalDate.of(2019, 9, 2), 0),
                Arguments.of(LocalDate.of(2015, 6, 15), LocalDate.of(2019, 9, 2), 4)
        );
    }

    @ParameterizedTest(name = "inception={0}, base={1} -> years={2}")
    @MethodSource("inceptionCases")
    void yearsSinceInception(LocalDate inception, LocalDate base, int expected) {
        assertEquals(expected, datesCalculator.yearsSinceInception(inception, base));
    }

    static Stream<Arguments> ageCases() {
        return Stream.of(
                Arguments.of(LocalDate.of(2001, 1, 11), BASE_DATE, 18),
                Arguments.of(LocalDate.of(1989, 9, 3), BASE_DATE, 29),
                Arguments.of(LocalDate.of(1989, 9, 1), BASE_DATE, 30),
                Arguments.of(LocalDate.of(1989, 9, 2), BASE_DATE, 30),
                Arguments.of(LocalDate.of(2019, 9, 2), BASE_DATE, 0),
                Arguments.of(LocalDate.of(2019, 9, 1), BASE_DATE, 0)
        );
    }

    @ParameterizedTest(name = "birth={0}, base={1} -> age={2}")
    @MethodSource("ageCases")
    void getAgeToDate(LocalDate birth, LocalDate base, int expectedAge) {
        assertEquals(expectedAge, datesCalculator.getAgeToDate(birth, base));
    }
}
