package org.lab2.systemarchitecturegroup5assignment3;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PetDTO(
        @NotBlank(message = "A pet name is required")
        @Size(max = 10, message = "Pet name cannot be longer than 10 characters")
        String name,

        @NotBlank(message = "A pet species is required")
        @Size(max = 50, message = "Pet species cannot be longer than 50 characters")
        String species,

        @NotNull(message = "Hunger level is required")
        @Min(value = 0, message = "Hunger level cannot be below 0")
        @Max(value = 100, message = "Hunger level cannot be above 100")
        Integer hungerLevel,

        @NotNull(message = "Happiness is required")
        @Min(value = 0, message = "Happiness cannot be below 0")
        @Max(value = 100, message = "Happiness cannot be above 100")
        Integer happiness
) {
}
