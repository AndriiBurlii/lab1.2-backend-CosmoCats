
package com.cosmocats.service;

import com.cosmocats.api.dto.ProductRequest;
import com.cosmocats.api.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse get(long id);
    List<ProductResponse> list();
    ProductResponse update(long id, ProductRequest request);
    void delete(long id);
}
