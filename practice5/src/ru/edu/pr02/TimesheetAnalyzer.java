package ru.edu.pr02;

/** Табель за пять рабочих дней, вариант 5. */
public final class TimesheetAnalyzer {
    public static final int DAYS = 5;
    public static final int MAX_HOURS = 30;
    public static final int DAY_NORM = 8;
    public static final int WEEK_NORM = 40;

    private TimesheetAnalyzer() { }

    public static int[] parseHours(String line) {
        if (line == null) throw new IllegalArgumentException("Строка табеля отсутствует");
        String[] parts = line.split(";", -1);
        if (parts.length != DAYS) {
            throw new IllegalArgumentException("Ожидалось 5 значений, получено: " + parts.length);
        }
        int[] hours = new int[DAYS];
        for (int i = 0; i < DAYS; i++) {
            String value = parts[i].trim();
            if (!value.matches("[+-]?[0-9]+")) {
                throw error(i, "ожидалось целое число");
            }
            try {
                hours[i] = Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                throw error(i, "число не помещается в int");
            }
            validateHour(hours[i], i);
        }
        return hours;
    }

    public static int total(int[] hours) {
        validate(hours);
        int result = 0;
        for (int hour : hours) result += hour;
        return result;
    }

    public static double average(int[] hours) {
        return (double) total(hours) / DAYS;
    }

    public static int overtimeDays(int[] hours) {
        validate(hours);
        int count = 0;
        for (int hour : hours) if (hour > DAY_NORM) count++;
        return count;
    }

    public static int weeklyOvertime(int[] hours) {
        return Math.max(0, total(hours) - WEEK_NORM);
    }

    private static void validate(int[] hours) {
        if (hours == null || hours.length != DAYS) {
            throw new IllegalArgumentException("Массив должен содержать 5 значений");
        }
        for (int i = 0; i < hours.length; i++) validateHour(hours[i], i);
    }

    private static void validateHour(int hour, int index) {
        if (hour < 0 || hour > MAX_HOURS) throw error(index, "допустимы часы 0.." + MAX_HOURS);
    }

    private static IllegalArgumentException error(int index, String message) {
        return new IllegalArgumentException("Позиция " + (index + 1) + ": " + message);
    }
}
