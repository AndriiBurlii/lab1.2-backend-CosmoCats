package com.cosmocats.repository;

import com.cosmocats.CosmoCatsApplication;
import com.cosmocats.config.PostgresTestConfig;
import com.cosmocats.domain.Category;
import com.cosmocats.domain.Order;
import com.cosmocats.domain.OrderLine;
import com.cosmocats.domain.Product;
import com.cosmocats.entity.OrderLineEntity;
import com.cosmocats.repository.projection.ProductSalesProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(
        classes = {CosmoCatsApplication.class, PostgresTestConfig.class}
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class DatabaseIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @BeforeEach
    void clean() {
        orderLineRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("CRUD: створення замовлення з лініями працює разом з PostgreSQL через Testcontainers")
    void createOrderWithLines_ok() {
        Category gadgets = categoryRepository.save(
                new Category(null, "gadgets", "Space Gadgets")
        );

        Product laserMouse = new Product(null, "Laser Mouse", new BigDecimal("99.99"), "gadgets");
        laserMouse.setCategoryEntity(gadgets);
        laserMouse = productRepository.save(laserMouse);

        Product rocketKeyboard = new Product(null, "Rocket Keyboard", new BigDecimal("149.50"), "gadgets");
        rocketKeyboard.setCategoryEntity(gadgets);
        rocketKeyboard = productRepository.save(rocketKeyboard);

        Order order = new Order(null, "ORD-001", "pilot@cosmo.cats", OffsetDateTime.now());
        order = orderRepository.save(order);

        OrderLine line1 = new OrderLine(null, laserMouse, 2, new BigDecimal("99.99"));
        line1.setOrder(order);

        OrderLine line2 = new OrderLine(null, rocketKeyboard, 1, new BigDecimal("149.50"));
        line2.setOrder(order);

        // зберігаємо entity, а не domain
        orderLineRepository.save(toEntity(line1));
        orderLineRepository.save(toEntity(line2));

        Order reloaded = orderRepository.findByNumber("ORD-001")
                .orElseThrow();

        assertThat(reloaded.getLines()).hasSize(2);
        assertThat(reloaded.getLines())
                .extracting(l -> l.getProduct().getName())
                .containsExactlyInAnyOrder("Laser Mouse", "Rocket Keyboard");
    }

    @Test
    @DisplayName("Унікальність продуктів в межах однієї категорії (name + category_id)")
    void uniqueProductNameWithinCategory_enforced() {
        Category food = categoryRepository.save(
                new Category(null, "food", "Space Food")
        );

        Product p1 = new Product(null, "Space Pizza", new BigDecimal("12.50"), "food");
        p1.setCategoryEntity(food);
        productRepository.saveAndFlush(p1);

        Product p2 = new Product(null, "Space Pizza", new BigDecimal("15.00"), "food");
        p2.setCategoryEntity(food);

        assertThatThrownBy(() -> productRepository.saveAndFlush(p2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Projections: топові продукти за кількістю купівель")
    void topSellingProducts_projectionWorks() {
        Category gadgets = categoryRepository.save(
                new Category(null, "gadgets", "Space Gadgets")
        );

        Product a = new Product(null, "A", new BigDecimal("10.00"), "gadgets");
        a.setCategoryEntity(gadgets);
        a = productRepository.save(a);

        Product b = new Product(null, "B", new BigDecimal("20.00"), "gadgets");
        b.setCategoryEntity(gadgets);
        b = productRepository.save(b);

        // Два замовлення
        Order o1 = orderRepository.save(
                new Order(null, "ORD-A", "a@cats.io", OffsetDateTime.now())
        );
        Order o2 = orderRepository.save(
                new Order(null, "ORD-B", "b@cats.io", OffsetDateTime.now())
        );

        // ВАЖЛИВО: жодних дублікатів (order_id, product_id)
        // o1: A (5 шт) + B (2 шт)
        // o2: A (3 шт)
        OrderLine l1 = new OrderLine(null, a, 5, new BigDecimal("10.00"));
        l1.setOrder(o1);

        OrderLine l2 = new OrderLine(null, b, 2, new BigDecimal("20.00"));
        l2.setOrder(o1);

        OrderLine l3 = new OrderLine(null, a, 3, new BigDecimal("10.00"));
        l3.setOrder(o2);

        // зберігаємо entity, а не domain
        orderLineRepository.saveAll(List.of(
                toEntity(l1),
                toEntity(l2),
                toEntity(l3)
        ));

        Page<ProductSalesProjection> page = productRepository.findTopSellingProducts(
                PageRequest.of(0, 10)
        );

        List<ProductSalesProjection> projections = page.getContent();
        assertThat(projections).hasSize(2);

        // A має 8 штук (5 + 3), B має 2
        assertThat(projections.get(0).productName()).isEqualTo("A");
        assertThat(projections.get(0).totalQuantity()).isEqualTo(8L);
        assertThat(projections.get(1).productName()).isEqualTo("B");
        assertThat(projections.get(1).totalQuantity()).isEqualTo(2L);
    }

    // --- простий маппер з domain OrderLine в JPA OrderLineEntity ---

    private OrderLineEntity toEntity(OrderLine line) {
        OrderLineEntity entity = new OrderLineEntity();
        entity.setId(line.getId());
        entity.setOrder(line.getOrder());
        entity.setProduct(line.getProduct());
        entity.setQty(line.getQty());
        entity.setPriceAtPurchase(line.getPriceAtPurchase());
        return entity;
    }
}
