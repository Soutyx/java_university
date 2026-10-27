package ru.edu.pr04;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class Order {
    // Параметр 30 принят как 30000; лимит проверяется при подтверждении.
    public static final BigDecimal MAX_TOTAL = new BigDecimal("30000");
    private final Employee employee;
    private final List<OrderItem> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.DRAFT;

    public Order(Employee employee) {
        if (employee == null) throw new IllegalArgumentException("Сотрудник отсутствует");
        this.employee = employee;
    }

    public void addProduct(Product product, int quantity) {
        ensureDraft();
        if (product == null) throw new IllegalArgumentException("Товар отсутствует");
        OrderItem.validateQuantity(quantity);
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            if (item.getProduct().getId().equals(product.getId())) {
                if (!item.getProduct().sameDefinition(product))
                    throw new IllegalArgumentException("Одинаковый id товара с разными названием или ценой");
                int combined = item.getQuantity() + quantity;
                // Сначала проверяем новую позицию, затем меняем список.
                OrderItem replacement = new OrderItem(product, combined);
                items.set(i, replacement);
                return;
            }
        }
        items.add(new OrderItem(product, quantity));
    }

    public void removeProduct(String productId) {
        ensureDraft();
        if (productId == null || productId.isBlank()) throw new IllegalArgumentException("Укажите id товара");
        if (!items.removeIf(item -> item.getProduct().getId().equals(productId.trim())))
            throw new IllegalArgumentException("Товар не найден: " + productId);
    }

    public BigDecimal totalAmount() {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderItem item : items) sum = sum.add(item.lineAmount());
        return sum;
    }

    public double total() { return totalAmount().doubleValue(); }

    public void confirm() {
        ensureDraft();
        if (items.isEmpty()) throw new IllegalStateException("Нельзя подтвердить пустой заказ");
        if (totalAmount().compareTo(MAX_TOTAL) > 0)
            throw new IllegalStateException("Сумма превышает лимит " + MAX_TOTAL.toPlainString());
        status = OrderStatus.CONFIRMED;
    }

    public List<OrderItem> getItems() { return List.copyOf(items); }
    public OrderStatus getStatus() { return status; }
    public Employee getEmployee() { return employee; }

    private void ensureDraft() {
        if (status != OrderStatus.DRAFT) throw new IllegalStateException("Изменять можно только черновик");
    }
}
