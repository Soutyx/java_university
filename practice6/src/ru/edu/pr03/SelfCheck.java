package ru.edu.pr03;


public final class SelfCheck {
    public static void main(String[] args) {
        ApprovalRouter router = new ApprovalRouter();
        check("Персональные данные важнее срочности", () -> {
            require(router.route(10000, true, RiskLevel.LOW, true) == ApprovalRoute.SECURITY_REVIEW);
        });
        check("Точная финансовая граница", () -> {
            require(router.route(499999, true, RiskLevel.LOW, false) == ApprovalRoute.FAST_TRACK);
            require(router.route(500000, true, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW);
            require(router.route(500001, false, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW);
        });
        check("Граница риска для варианта 5", () -> {
            require(router.route(149999, true, RiskLevel.HIGH, false) == ApprovalRoute.FAST_TRACK);
            require(router.route(150000, true, RiskLevel.HIGH, false) == ApprovalRoute.DIRECTOR_REVIEW);
            require(router.route(150001, false, RiskLevel.HIGH, false) == ApprovalRoute.DIRECTOR_REVIEW);
        });
        check("Риск важнее финансового порога", () -> {
            require(router.route(500000, true, RiskLevel.HIGH, false) == ApprovalRoute.DIRECTOR_REVIEW);
        });
        check("Персональные данные важнее риска", () -> {
            require(router.route(500000, true, RiskLevel.HIGH, true) == ApprovalRoute.SECURITY_REVIEW);
        });
        check("Нулевая сумма и обычный маршрут", () -> {
            require(router.route(0, false, RiskLevel.LOW, false) == ApprovalRoute.STANDARD);
        });
        check("Отрицательная сумма и отсутствующий риск", () -> {
            expect(IllegalArgumentException.class, () -> router.route(-1, false, RiskLevel.LOW, false));
            expect(IllegalArgumentException.class, () -> router.route(1, false, null, true));
        });
        check("Максимальная сумма long", () -> {
            require(router.route(Long.MAX_VALUE, false, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW);
        });
        finish();
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
