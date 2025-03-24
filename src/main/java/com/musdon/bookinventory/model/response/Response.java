package com.musdon.bookinventory.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musdon.bookinventory.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private String responseMessage;
    private BookInventoryResponse bookInventory;
    private List<BookInventoryResponse> bookInventoryList= new ArrayList<>();
    private Integer totalBooks;
    private CartResponse cartItem;
    private List<CartResponse> cartItemList= new ArrayList<>();
}
