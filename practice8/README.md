# Практическая работа 8

Инкапсуляция, enum и управление состоянием объекта.

Заявка на командировку меняет статус через submit, approve, reject, cancel и complete. SelfCheck проверяет допустимые переходы, даты и причину отклонения.

Сборка без Maven (JDK 17 или новее):

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out ru.edu.pr05.SelfCheck
```


