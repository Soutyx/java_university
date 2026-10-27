package ru.edu.pr03;

public final class ApprovalRouter {
    public static final long FINANCE_LIMIT = 500_000L;
    // Параметр 30 трактуется как процент, по примеру репозитория.
    public static final int RISK_PRIORITY_PERCENT = 30;
    public static final long DIRECTOR_LIMIT = FINANCE_LIMIT * RISK_PRIORITY_PERCENT / 100;

    public ApprovalRoute route(long amount, boolean urgent, RiskLevel risk, boolean containsPersonalData) {
        if (amount < 0) throw new IllegalArgumentException("Сумма не может быть отрицательной");
        if (risk == null) throw new IllegalArgumentException("Укажите уровень риска");
        if (containsPersonalData) return ApprovalRoute.SECURITY_REVIEW;
        if (requiresDirector(amount, risk)) return ApprovalRoute.DIRECTOR_REVIEW;
        if (amount >= FINANCE_LIMIT) return ApprovalRoute.FINANCE_REVIEW;
        if (urgent) return ApprovalRoute.FAST_TRACK;
        return ApprovalRoute.STANDARD;
    }

    private boolean requiresDirector(long amount, RiskLevel risk) {
        return risk == RiskLevel.HIGH && amount >= DIRECTOR_LIMIT;
    }
}
