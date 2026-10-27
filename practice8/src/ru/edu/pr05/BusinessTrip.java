package ru.edu.pr05;

import java.time.LocalDate;

public final class BusinessTrip {
    public static final int MIN_REJECTION_REASON_LENGTH = 10;
    private final String id;
    private final LocalDate start;
    private final LocalDate end;
    private TripStatus status = TripStatus.DRAFT;
    private String rejectionReason;

    public BusinessTrip(String id, LocalDate start, LocalDate end) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Укажите id командировки");
        if (start == null || end == null) throw new IllegalArgumentException("Укажите даты начала и окончания");
        if (end.isBefore(start)) throw new IllegalArgumentException("Окончание не может быть раньше начала");
        this.id = id.trim();
        this.start = start;
        this.end = end;
    }

    public void submit() {
        requireStatus(TripStatus.DRAFT, "submit");
        status = TripStatus.SUBMITTED;
    }

    public void approve() {
        requireStatus(TripStatus.SUBMITTED, "approve");
        status = TripStatus.APPROVED;
    }

    public void reject(String reason) {
        requireStatus(TripStatus.SUBMITTED, "reject");
        String normalized = reason == null ? "" : reason.trim();
        if (normalized.length() < MIN_REJECTION_REASON_LENGTH)
            throw new IllegalArgumentException("Причина должна содержать минимум 10 символов без крайних пробелов");
        rejectionReason = normalized;
        status = TripStatus.REJECTED;
    }

    /** Отмена по явно заданному правилу PDF; смысл параметра 30 не раскрыт. */
    public void cancel() {
        if (status != TripStatus.DRAFT && status != TripStatus.SUBMITTED && status != TripStatus.APPROVED)
            throw new IllegalStateException("Отмена недоступна из " + status);
        status = TripStatus.CANCELLED;
    }

    public void complete(LocalDate today) {
        requireStatus(TripStatus.APPROVED, "complete");
        if (today == null) throw new IllegalArgumentException("Укажите дату завершения");
        if (!today.isAfter(end)) throw new IllegalStateException("Завершить можно только после даты окончания");
        status = TripStatus.COMPLETED;
    }

    public String getId() { return id; }
    public LocalDate getStart() { return start; }
    public LocalDate getEnd() { return end; }
    public TripStatus getStatus() { return status; }
    public String getRejectionReason() { return rejectionReason; }

    private void requireStatus(TripStatus expected, String action) {
        if (status != expected) throw new IllegalStateException(action + " недоступен из " + status);
    }
}
