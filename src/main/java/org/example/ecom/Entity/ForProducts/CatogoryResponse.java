package org.example.ecom.Entity.ForProducts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CatogoryResponse {
    private String name;
    private String Url;
    private List<CatogoryResponse> children;
}
