package com.scale.global.insurance.app.engine.impl;

import com.scale.global.insurance.app.engine.TariffRate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TariffRateImpl.class)
class TariffRateImplTest {

    @Autowired
    TariffRate tariffRate;

    static Stream<Arguments> tariffBrackets() {
        return Stream.of(
                Arguments.of(0, new BigDecimal("0.5")),
                Arguments.of(5, new BigDecimal("0.5")),
                Arguments.of(10, new BigDecimal("0.5")),
                Arguments.of(11, new BigDecimal("0.5")),
                Arguments.of(12, new BigDecimal("0.75")),
                Arguments.of(15, new BigDecimal("0.75")),
                Arguments.of(19, new BigDecimal("0.75")),
                Arguments.of(20, new BigDecimal("0.75")),
                Arguments.of(21, new BigDecimal("1.00")),
                Arguments.of(30, new BigDecimal("1.00")),
                Arguments.of(43, new BigDecimal("1.00")),
                Arguments.of(44, new BigDecimal("1.00")),
                Arguments.of(45, new BigDecimal("1.25")),
                Arguments.of(55, new BigDecimal("1.25")),
                Arguments.of(65, new BigDecimal("1.25")),
                Arguments.of(66, new BigDecimal("1.25")),
                Arguments.of(67, new BigDecimal("0.0")),
                Arguments.of(80, new BigDecimal("0.0")),
                Arguments.of(100, new BigDecimal("0.0"))
        );
    }

    @ParameterizedTest(name = "age={0} -> rate={1}")
    @MethodSource("tariffBrackets")
    void getRateForAge(int age, BigDecimal expectedRate) {
        assertEquals(expectedRate, tariffRate.getRate(age));
    }

    @Test
    void getProgramPrice() {
        BigDecimal programPrice = tariffRate.getProgramPrice();
        assertEquals(new BigDecimal("300.00"), programPrice);
    }
}
