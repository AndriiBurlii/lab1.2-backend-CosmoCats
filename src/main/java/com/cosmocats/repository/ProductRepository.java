package com.cosmocats.repository;

import com.cosmocats.domain.Category;
import com.cosmocats.domain.Product;
import com.cosmocats.repository.projection.ProductSalesProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);

    // create: перевірка у категорії
    boolean existsByNameIgnoreCaseAndCategory(String name, Category category);

    // update: те саме, але виключаючи поточний id
    boolean existsByNameIgnoreCaseAndCategoryAndIdNot(String name, Category category, Long id);

    @Query("""
        select new com.cosmocats.repository.projection.ProductSalesProjection(
            p.id, p.name, sum(l.qty)
        )
        from OrderLine l join l.product p
        group by p.id, p.name
        order by sum(l.qty) desc
        """)
    Page<ProductSalesProjection> findTopSellingProducts(Pageable pageable);
}

