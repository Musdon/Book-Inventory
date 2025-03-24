package com.musdon.bookinventory.model.response;

import com.musdon.bookinventory.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookInventoryResponse {
    private Long id;
    private String title;
    private Genre genre;
    private String author;
    private String isbnCode;
    private Integer yearOfPublication;
    private Double price;
}
