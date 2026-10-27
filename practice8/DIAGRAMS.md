# Диаграммы

## Activity

```mermaid
flowchart TD
    start([Вызвать доменный метод]) --> allowed{Статус разрешает выбранную операцию?}
    allowed -- нет --> state[IllegalStateException; состояние не меняется]
    allowed -- да --> operation{Операция}
    operation -- reject --> reason{Причина после trim содержит от 10 символов?}
    reason -- нет --> argument[IllegalArgumentException; состояние не меняется]
    reason -- да --> rejected[Сохранить причину; установить REJECTED]
    operation -- complete --> date{Дата задана?}
    date -- нет --> argument
    date -- да --> endDate{Дата строго позже окончания?}
    endDate -- нет --> state
    endDate -- да --> completed[Установить COMPLETED]
    operation -- submit --> submitted[Установить SUBMITTED]
    operation -- approve --> approved[Установить APPROVED]
    operation -- cancel --> cancelled[Установить CANCELLED]
    rejected --> stop([Готово])
    completed --> stop
    submitted --> stop
    approved --> stop
    cancelled --> stop
```

## Классы

```mermaid
classDiagram
    class BusinessTrip {
        -String id
        -LocalDate start
        -LocalDate end
        -TripStatus status
        -String rejectionReason
        +int MIN_REJECTION_REASON_LENGTH = 10
        +submit() void
        +approve() void
        +reject(String) void
        +cancel() void
        +complete(LocalDate) void
        +getStatus() TripStatus
    }
    class TripStatus {
        <<enumeration>>
        DRAFT
        SUBMITTED
        APPROVED
        REJECTED
        CANCELLED
        COMPLETED
    }
    class SelfCheck
    BusinessTrip --> TripStatus
    SelfCheck ..> BusinessTrip : проверяет
```

## Переходы состояний

| Статус | submit | approve | reject | cancel | complete |
|---|---|---|---|---|---|
| DRAFT | SUBMITTED | ошибка | ошибка | CANCELLED | ошибка |
| SUBMITTED | ошибка | APPROVED | REJECTED | CANCELLED | ошибка |
| APPROVED | ошибка | ошибка | ошибка | CANCELLED | COMPLETED |
| REJECTED | ошибка | ошибка | ошибка | ошибка | ошибка |
| CANCELLED | ошибка | ошибка | ошибка | ошибка | ошибка |
| COMPLETED | ошибка | ошибка | ошибка | ошибка | ошибка |

Для reject нужна причина от 10 символов после trim. Для complete дата должна быть строго позже окончания поездки.
