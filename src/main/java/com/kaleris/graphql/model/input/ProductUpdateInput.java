package com.kaleris.graphql.model.input;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateInput {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}
