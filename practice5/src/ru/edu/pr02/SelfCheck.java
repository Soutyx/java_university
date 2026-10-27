package ru.edu.pr02;


public final class SelfCheck {
    public static void main(String[] args) {
        check("Обычная неделя", () -> {
            int[] hours = TimesheetAnalyzer.parseHours("8;8;8;8;8");
            require(TimesheetAnalyzer.total(hours) == 40);
            require(TimesheetAnalyzer.average(hours) == 8.0);
            require(TimesheetAnalyzer.weeklyOvertime(hours) == 0);
        });
        check("Подсчёт переработки", () -> {
            int[] hours = TimesheetAnalyzer.parseHours("9;8;7;10;8");
            require(TimesheetAnalyzer.total(hours) == 42);
            require(TimesheetAnalyzer.average(hours) == 8.4);
            require(TimesheetAnalyzer.overtimeDays(hours) == 2);
            require(TimesheetAnalyzer.weeklyOvertime(hours) == 2);
        });
        check("Границы 0 и 30", () -> {
            require(TimesheetAnalyzer.total(TimesheetAnalyzer.parseHours("0;0;0;0;0")) == 0);
            require(TimesheetAnalyzer.total(TimesheetAnalyzer.parseHours("30;30;30;30;30")) == 150);
        });
        check("17 допустимо, 31 уже нет", () -> {
            require(TimesheetAnalyzer.parseHours("8;8;17;8;8")[2] == 17);
            expect(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;8;31;8;8"));
        });
        check("Пустая строка и отсутствие строки", () -> {
            expect(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours(""));
            expect(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours(null));
        });
        check("Неверное число полей", () -> {
            expect(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;8;8;8"));
            expect(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;8;8;8;"));
        });
        check("Неверное число и позиция ошибки", () -> {
            try {
                TimesheetAnalyzer.parseHours("8;abc;8;8;8");
                throw new AssertionError("Ошибка не обнаружена");
            } catch (IllegalArgumentException error) {
                require(error.getMessage().contains("2"));
            }
            expect(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;-1;8;8;8"));
        });
        check("Проверка массива при прямом вызове", () -> {
            expect(IllegalArgumentException.class, () -> TimesheetAnalyzer.total(null));
            expect(IllegalArgumentException.class, () -> TimesheetAnalyzer.total(new int[]{8, 8, 31, 8, 8}));
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
