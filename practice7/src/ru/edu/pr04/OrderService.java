package ru.edu.pr04;

public final class OrderService {
    public Order createOrder(Employee employee) { return new Order(employee); }
    public void validateForConfirm(Order order) {
        if (order == null) throw new IllegalArgumentException("Заказ отсутствует");
        order.confirm();
    }
}
