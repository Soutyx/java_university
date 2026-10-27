package ru.edu.pr02;

public final class BoundaryTests {
public static void main(String[] args) {

check("Нормальная неделя", () -> { int[] h=TimesheetAnalyzer.parseHours("8;8;8;8;8"); eq(40,TimesheetAnalyzer.total(h)); eq(8.0,TimesheetAnalyzer.average(h)); eq(0,TimesheetAnalyzer.weeklyOvertime(h)); });
check("Сумма, среднее и переработка", () -> { int[] h=TimesheetAnalyzer.parseHours("9;8;7;10;8"); eq(42,TimesheetAnalyzer.total(h)); eq(8.4,TimesheetAnalyzer.average(h)); eq(2,TimesheetAnalyzer.overtimeDays(h)); eq(2,TimesheetAnalyzer.weeklyOvertime(h)); });
check("Нижняя граница 0", () -> eq(0,TimesheetAnalyzer.total(TimesheetAnalyzer.parseHours("0;0;0;0;0"))));
check("Верхняя граница 30", () -> eq(150,TimesheetAnalyzer.total(TimesheetAnalyzer.parseHours("30;30;30;30;30"))));
check("17 разрешено вариантом 5", () -> eq(17,TimesheetAnalyzer.parseHours("8;8;17;8;8")[2]));
check("31 отклоняется", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;8;31;8;8")));
check("Отрицательные часы", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("-1;8;8;8;8")));
check("Пустая строка", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("")));
check("Отсутствующая строка", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours(null)));
check("Четыре значения", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;8;8;8")));
check("Шесть значений", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;8;8;8;8;8")));
check("Пустое последнее поле не теряется", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;8;8;8;")));
check("Дробное число", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;8.5;8;8;8")));
check("Текст вместо числа", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;abc;8;8;8")));
check("Переполнение int", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.parseHours("8;999999999999999999;8;8;8")));
check("Номер позиции в сообщении", () -> { try { TimesheetAnalyzer.parseHours("8;abc;8;8;8"); throw new AssertionError(); } catch(IllegalArgumentException e) { if(!e.getMessage().contains("2")) throw new AssertionError(e); } });
check("Пробелы вокруг чисел", () -> eq(40,TimesheetAnalyzer.total(TimesheetAnalyzer.parseHours(" 8 ; 8 ; 8 ; 8 ; 8 "))));
check("Повтор часов разрешён", () -> eq(5,TimesheetAnalyzer.overtimeDays(TimesheetAnalyzer.parseHours("9;9;9;9;9"))));
check("Дневная норма включительно", () -> eq(0,TimesheetAnalyzer.overtimeDays(new int[]{8,8,8,8,8})));
check("Недельная переработка не отрицательна", () -> eq(0,TimesheetAnalyzer.weeklyOvertime(new int[]{1,1,1,1,1})));
check("Прямой вызов с null массивом", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.total(null)));
check("Прямой вызов с пустым массивом", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.average(new int[0])));
check("Прямой вызов обойти диапазон нельзя", () -> fails(IllegalArgumentException.class, () -> TimesheetAnalyzer.overtimeDays(new int[]{8,8,31,8,8})));

finish();
}

    private static int passed;
    private static int failed;
    private static void check(String name, Runnable action) {
        try { action.run(); passed++; System.out.println("PASS " + name); }
        catch (Throwable ex) { failed++; System.out.println("FAIL " + name + ": " + ex); }
    }
    private static void eq(Object expected, Object actual) {
        if (!java.util.Objects.equals(expected, actual)) throw new AssertionError(expected + " != " + actual);
    }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable ex) {
            if (type.isInstance(ex)) return;
            throw new AssertionError("Wrong exception: " + ex, ex);
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
    private static void finish() {
        System.out.println("RESULT passed=" + passed + " failed=" + failed);
        if (failed > 0) throw new AssertionError("Failed checks: " + failed);
    }
}
