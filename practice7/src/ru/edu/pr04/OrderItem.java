package ru.edu.pr04;

import java.math.BigDecimal;

/** Неизменяемая позиция заказа. */
public final class OrderItem {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 100;
    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("Товар отсутствует");
        validateQuantity(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    public static void validateQuantity(int quantity) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY)
            throw new IllegalArgumentException("Количество должно быть от 1 до 100");
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal lineAmount() { return product.priceAmount().multiply(BigDecimal.valueOf(quantity)); }
    public double getLineTotal() { return lineAmount().doubleValue(); }
}
