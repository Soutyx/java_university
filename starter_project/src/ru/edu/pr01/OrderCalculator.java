package ru.edu.pr01;

import java.util.Scanner;

public class OrderCalculator {

    public static final double MAX_DISCOUNT = 30.0;
    public static final double VAT_RATE = 20.0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка: Некорректный формат количества.");
            return;
        }
        int quantity = scanner.nextInt();

        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: Некорректный формат цены.");
            return;
        }
        // Исправлено: nextDouble() вместо getAsDouble()
        double price = scanner.nextDouble();

        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: Некорректный формат скидки.");
            return;
        }
        // Исправлено: nextDouble() вместо getAsDouble()
        double discountPercent = scanner.nextDouble();

        if (!isValid(quantity, price, discountPercent)) {
            System.out.println("Ошибка: Входные данные выходят за допустимые диапазоны.");
            return;
        }

        double basePrice = calculateBase(quantity, price);
        double discountAmount = applyDiscount(basePrice, discountPercent);
        double discountedPrice = basePrice - discountAmount;
        double vatAmount = calculateVat(discountedPrice, VAT_RATE);
        double totalPrice = calculateTotal(quantity, price, discountPercent, VAT_RATE);

        System.out.printf("Базовая стоимость: %.2f руб.%n", basePrice);
        System.out.printf("Скидка (%.1f%%): %.2f руб.%n", discountPercent, discountAmount);
        System.out.printf("НДС (%.1f%%): %.2f руб.%n", VAT_RATE, vatAmount);
        System.out.printf("Итого к оплате: %.2f руб.%n", totalPrice);
    }

    public static boolean isValid(int quantity, double price, double discount) {
        if (quantity <= 0 || quantity > 10000) {
            return false;
        }
        if (price <= 0 || price > 5000000) {
            return false;
        }
        if (discount < 0 || discount > MAX_DISCOUNT) {
            return false;
        }
        return true;
    }

    public static double calculateBase(int quantity, double price) {
        return quantity * price;
    }

    public static double applyDiscount(double basePrice, double discountPercent) {
        return basePrice * (discountPercent / 100.0);
    }

    public static double calculateVat(double discountedPrice, double vatRate) {
        return discountedPrice * (vatRate / 100.0);
    }

    // Метод, необходимый для успешного прохождения автопроверки SelfCheck.java
    public static double calculateTotal(int quantity, double price, double discountPercent, double vatRate) {
        double base = calculateBase(quantity, price);
        double discountAmount = applyDiscount(base, discountPercent);
        double discountedPrice = base - discountAmount;
        double vatAmount = calculateVat(discountedPrice, vatRate);
        return discountedPrice + vatAmount;
    }
}