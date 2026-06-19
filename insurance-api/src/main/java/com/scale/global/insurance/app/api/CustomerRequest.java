package com.scale.global.insurance.app.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CustomerRequest(
        @Size(min = 2, max = 100, message = "Min size is 2 and max size is 100")
        String firstName,

        @Size(min = 2, max = 100, message = "Min size is 2 and max size is 100")
        String lastName,

        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @PastOrPresent(message = "Date should be in past or to be present date")
        LocalDate dateOfBirth,

        @JsonFormat(pattern = "dd-MM-yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @PastOrPresent(message = "Date should be in past or to be present date")
        LocalDate inceptionOfThePolicy
) {}
