package com.scale.global.insurance.app.engine.impl;

import com.scale.global.insurance.app.engine.DatesCalculator;
import com.scale.global.insurance.app.engine.PriceCalculator;
import com.scale.global.insurance.app.engine.TariffRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PriceCalculatorImplTest {

    @Mock
    TariffRate tariffRate;

    @Mock
    DatesCalculator datesCalculator;

    PriceCalculator priceCalculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        priceCalculator = new PriceCalculatorImpl(tariffRate, datesCalculator);
        when(tariffRate.getProgramPrice()).thenReturn(new BigDecimal("300.00"));
    }

    static Stream<Arguments> rateCalculationCases() {
        return Stream.of(
                // age=29, rate=1.0, yearsInception=9 -> (1.0*300)*(100-9)/100 = 300*0.91 = 273.00
                Arguments.of(29, new BigDecimal("1.0"), 9, new BigDecimal("273.00")),
                // age=69, rate=0.0, yearsInception=39 -> (0.0*300)*(100-39)/100 = 0.00
                Arguments.of(69, new BigDecimal("0.0"), 39, new BigDecimal("0.00")),
                // age=59, rate=1.25, yearsInception=19 -> (1.25*300)*(100-19)/100 = 375*0.81 = 303.75
                Arguments.of(59, new BigDecimal("1.25"), 19, new BigDecimal("303.75")),
                // age=35, rate=1.00, yearsInception=11 -> (1.00*300)*(100-11)/100 = 300*0.89 = 267.00
                Arguments.of(35, new BigDecimal("1.00"), 11, new BigDecimal("267.00")),
                // age=66, rate=1.25, yearsInception=33 -> (1.25*300)*(100-33)/100 = 375*0.67 = 251.25
                Arguments.of(66, new BigDecimal("1.25"), 33, new BigDecimal("251.25"))
        );
    }

    @ParameterizedTest(name = "age={0}, rate={1}, years={2} -> price={3}")
    @MethodSource("rateCalculationCases")
    void calculateRate(int age, BigDecimal rate, int yearsInception, BigDecimal expectedPrice) {
        when(datesCalculator.getAgeToDate(any(), any())).thenReturn(age);
        when(datesCalculator.yearsSinceInception(any(), any())).thenReturn(yearsInception);
        when(tariffRate.getRate(age)).thenReturn(rate);

        BigDecimal price = priceCalculator.calculateRate(LocalDate.now(), LocalDate.now());
        assertEquals(expectedPrice, price);
    }

    @Test
    void calculateRateWithZeroYearsInception() {
        when(datesCalculator.getAgeToDate(any(), any())).thenReturn(25);
        when(datesCalculator.yearsSinceInception(any(), any())).thenReturn(0);
        when(tariffRate.getRate(25)).thenReturn(new BigDecimal("1.00"));

        BigDecimal price = priceCalculator.calculateRate(LocalDate.now(), LocalDate.now());
        assertEquals(new BigDecimal("300.00"), price);
    }
}
