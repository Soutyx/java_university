package ru.edu.pr05;

import java.time.LocalDate;

public final class SelfCheck {
    public static void main(String[] args) {
        check("Отправка и согласование", () -> {
            BusinessTrip trip = trip();
            trip.submit();
            trip.approve();
            require(trip.getStatus() == TripStatus.APPROVED);
        });
        check("Отмена из трёх допустимых статусов", () -> {
            BusinessTrip draft = trip();
            draft.cancel();
            BusinessTrip submitted = trip();
            submitted.submit();
            submitted.cancel();
            BusinessTrip approved = approved();
            approved.cancel();
            require(draft.getStatus() == TripStatus.CANCELLED);
            require(submitted.getStatus() == TripStatus.CANCELLED);
            require(approved.getStatus() == TripStatus.CANCELLED);
        });
        check("Пустая и короткая причина", () -> {
            BusinessTrip trip = trip();
            trip.submit();
            expect(IllegalArgumentException.class, () -> trip.reject(null));
            expect(IllegalArgumentException.class, () -> trip.reject("          "));
            expect(IllegalArgumentException.class, () -> trip.reject("123456789"));
            require(trip.getStatus() == TripStatus.SUBMITTED);
        });
        check("Причина ровно из 10 символов", () -> {
            BusinessTrip trip = trip();
            trip.submit();
            trip.reject(" 1234567890 ");
            require(trip.getStatus() == TripStatus.REJECTED);
            require(trip.getRejectionReason().equals("1234567890"));
            expect(IllegalStateException.class, trip::cancel);
        });
        check("Даты и обязательные поля", () -> {
            expect(IllegalArgumentException.class, () -> new BusinessTrip("", START, END));
            expect(IllegalArgumentException.class, () -> new BusinessTrip("T", null, END));
            expect(IllegalArgumentException.class, () -> new BusinessTrip("T", END, START));
            require(new BusinessTrip("T", START, START).getStatus() == TripStatus.DRAFT);
        });
        check("Завершение только после даты окончания", () -> {
            BusinessTrip trip = approved();
            expect(IllegalStateException.class, () -> trip.complete(END.minusDays(1)));
            expect(IllegalStateException.class, () -> trip.complete(END));
            expect(IllegalArgumentException.class, () -> trip.complete(null));
            require(trip.getStatus() == TripStatus.APPROVED);
            trip.complete(END.plusDays(1));
            require(trip.getStatus() == TripStatus.COMPLETED);
        });
        check("Завершённую и отменённую заявку менять нельзя", () -> {
            BusinessTrip completed = approved();
            completed.complete(END.plusDays(1));
            expect(IllegalStateException.class, completed::cancel);
            expect(IllegalStateException.class, completed::submit);
            BusinessTrip cancelled = trip();
            cancelled.cancel();
            expect(IllegalStateException.class, cancelled::cancel);
            expect(IllegalStateException.class, cancelled::submit);
        });
        check("Недопустимый и повторный переход", () -> {
            BusinessTrip trip = trip();
            expect(IllegalStateException.class, trip::approve);
            trip.submit();
            expect(IllegalStateException.class, trip::submit);
            trip.approve();
            expect(IllegalStateException.class, trip::approve);
        });
        finish();
    }

    private static final LocalDate START = LocalDate.of(2026, 6, 1);
    private static final LocalDate END = LocalDate.of(2026, 6, 5);

    private static BusinessTrip trip() {
        return new BusinessTrip("T1", START, END);
    }

    private static BusinessTrip approved() {
        BusinessTrip trip = trip();
        trip.submit();
        trip.approve();
        return trip;
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
