package com.scale.global.insurance.app.converters;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LocalDateAttributeConverterTest {

    private final LocalDateAttributeConverter converter = new LocalDateAttributeConverter();

    @Test
    void convertToDatabaseColumn() {
        LocalDate localDate = LocalDate.of(2023, 6, 15);
        Date result = converter.convertToDatabaseColumn(localDate);
        assertEquals(Date.valueOf(localDate), result);
    }

    @Test
    void convertToDatabaseColumnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute() {
        Date sqlDate = Date.valueOf(LocalDate.of(2023, 6, 15));
        LocalDate result = converter.convertToEntityAttribute(sqlDate);
        assertEquals(LocalDate.of(2023, 6, 15), result);
    }

    @Test
    void convertToEntityAttributeNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }
}
