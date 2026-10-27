package ru.edu.pr04;

import java.math.BigDecimal;

public final class Product {
    private final String id;
    private final String name;
    private final BigDecimal price;

    public Product(String id, String name, double price) {
        this(id, name, decimal(price));
    }

    public Product(String id, String name, BigDecimal price) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Укажите id товара");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Укажите название товара");
        if (price == null || price.signum() <= 0) throw new IllegalArgumentException("Цена должна быть положительной");
        this.id = id.trim();
        this.name = name.trim();
        this.price = price.stripTrailingZeros();
    }

    private static BigDecimal decimal(double price) {
        if (!Double.isFinite(price)) throw new IllegalArgumentException("Цена должна быть конечным числом");
        return BigDecimal.valueOf(price);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price.doubleValue(); }
    public BigDecimal priceAmount() { return price; }

    public boolean sameDefinition(Product other) {
        return other != null && id.equals(other.id) && name.equals(other.name)
            && price.compareTo(other.price) == 0;
    }
}
