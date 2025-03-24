package com.musdon.bookinventory.model.request;

import com.musdon.bookinventory.model.Genre;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookRequest {
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Title must contain only numbers, letters, and spaces.")
    @NotNull(message = "title cannot be null")
    private String title;
    @NotNull(message = "Genre must be provided and one of the predefined options.")
    private Genre genre;
    @NotNull(message = "author cannot be null")
    @Size(min = 3)
    private String author;
    @NotNull(message = "isbn cannot be null")
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN code must contain only numbers and dashes.")
    private String isbnCode;
    @NotNull(message = "yearOfPublication cannot be null")
    private Integer yearOfPublication;
    @NotNull(message = "price cannot be null")
    private Double price;
}
