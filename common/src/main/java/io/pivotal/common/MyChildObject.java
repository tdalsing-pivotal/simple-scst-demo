package io.pivotal.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class MyChildObject {

    @NotBlank
    String description;

    @Valid
    MyChildObject childObject;
}
