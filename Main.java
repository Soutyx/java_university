package ru.edu.pr02;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 2 && args[0].equals("--file")) {
            for (String line : Files.readAllLines(Path.of(args[1]), StandardCharsets.UTF_8)) report(line);
        } else if (args.length > 0) {
            report(String.join(" ", args));
        } else {
            System.out.println("Введите 5 целых значений через ; (0..30):");
            report(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)).readLine());
        }
    }

    private static void report(String line) {
        try {
            int[] h = TimesheetAnalyzer.parseHours(line);
            System.out.printf(java.util.Locale.ROOT,
                "%s -> сумма=%d; среднее=%.2f; дней переработки=%d; недельная переработка=%d%n",
                line, TimesheetAnalyzer.total(h), TimesheetAnalyzer.average(h),
                TimesheetAnalyzer.overtimeDays(h), TimesheetAnalyzer.weeklyOvertime(h));
        } catch (IllegalArgumentException ex) {
            System.out.println("Ошибка: " + ex.getMessage());
        }
    }
}
