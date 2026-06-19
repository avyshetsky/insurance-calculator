package com.scale.global.insurance.app.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CustomerResponse(
        Integer insuranceNumber,
        String firstName,
        String lastName,

        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate dateOfBirth,

        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate inceptionOfThePolicy,

        BigDecimal rate
) {}
