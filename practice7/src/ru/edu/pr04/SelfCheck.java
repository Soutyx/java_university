package ru.edu.pr04;


public final class SelfCheck {
    public static void main(String[] args) {
        check("Типовой заказ", () -> {
            Order order = order();
            order.addProduct(product(1000), 2);
            require(order.total() == 2000);
            require(order.getItems().size() == 1);
        });
        check("Количество от 1 до 100", () -> {
            Order order = order();
            order.addProduct(product(1), 1);
            order.addProduct(product(1), 99);
            require(order.getItems().get(0).getQuantity() == 100);
            expect(IllegalArgumentException.class, () -> order.addProduct(product(1), 1));
            require(order.total() == 100);
            expect(IllegalArgumentException.class, () -> order().addProduct(product(1), 0));
            expect(IllegalArgumentException.class, () -> order().addProduct(product(1), 101));
        });
        check("Проверки товара", () -> {
            expect(IllegalArgumentException.class, () -> new Product("", "Мышь", 1000));
            expect(IllegalArgumentException.class, () -> new Product("P", "", 1000));
            expect(IllegalArgumentException.class, () -> product(0));
            expect(IllegalArgumentException.class, () -> product(Double.NaN));
        });
        check("Слияние товара и конфликт цены", () -> {
            Order order = order();
            order.addProduct(product(1000), 2);
            order.addProduct(product(1000), 1);
            require(order.getItems().size() == 1 && order.total() == 3000);
            expect(IllegalArgumentException.class, () -> order.addProduct(product(2000), 1));
            require(order.total() == 3000);
        });
        check("Удаление позиции", () -> {
            Order order = order();
            order.addProduct(product(1000), 1);
            order.removeProduct("P");
            require(order.total() == 0);
            expect(IllegalArgumentException.class, () -> order.removeProduct("P"));
            expect(IllegalStateException.class, order::confirm);
        });
        check("Лимит при подтверждении", () -> {
            Order exact = order();
            exact.addProduct(product(30000), 1);
            exact.confirm();
            require(exact.getStatus() == OrderStatus.CONFIRMED);
            Order excess = order();
            excess.addProduct(product(30000.01), 1);
            expect(IllegalStateException.class, excess::confirm);
            require(excess.getStatus() == OrderStatus.DRAFT);
        });
        check("После подтверждения нельзя менять заказ", () -> {
            Order order = order();
            order.addProduct(product(1000), 1);
            order.confirm();
            expect(IllegalStateException.class, () -> order.addProduct(product(1000), 1));
            expect(IllegalStateException.class, () -> order.removeProduct("P"));
            expect(IllegalStateException.class, order::confirm);
        });
        check("Защищённые позиции и точная сумма", () -> {
            Order order = order();
            order.addProduct(product(0.1), 1);
            var snapshot = order.getItems();
            order.addProduct(product(0.1), 2);
            require(snapshot.get(0).getQuantity() == 1);
            require(order.totalAmount().compareTo(new java.math.BigDecimal("0.3")) == 0);
            expect(UnsupportedOperationException.class, () -> order.getItems().clear());
        });
        finish();
    }

    private static Order order() {
        return new OrderService().createOrder(new Employee("E1", "Тестовый сотрудник"));
    }

    private static Product product(double price) {
        return new Product("P", "Мышь", price);
    }

    private static int passed;
    private static int failed;

    private static void check(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("OK  " + name);
        } catch (RuntimeException | AssertionError error) {
            failed++;
            System.out.println("FAIL " + name + ": " + error.getMessage());
        }
    }

    private static void require(boolean condition) {
        if (!condition) throw new AssertionError("Неверный результат");
    }

    private static void expect(Class<? extends RuntimeException> type, Runnable action) {
        try {
            action.run();
        } catch (RuntimeException error) {
            if (type.isInstance(error)) return;
            throw error;
        }
        throw new AssertionError("Ожидалось исключение " + type.getSimpleName());
    }

    private static void finish() {
        System.out.println("PASS: " + passed + "; FAIL: " + failed);
        if (failed != 0) throw new AssertionError("Есть непройденные проверки");
    }
}
