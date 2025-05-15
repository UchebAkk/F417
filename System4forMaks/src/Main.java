import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class DebugHelper {
    private static final Logger logger = Logger.getLogger(DebugHelper.class.getName());

    static {
        try {
            // Настройка логгера для записи в файл app.log
            FileHandler fileHandler = new FileHandler("app.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            // Отключаем вывод логов в консоль (опционально)
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Не удалось настроить логгер: " + e.getMessage());
        }
    }
    public static void logInfo(String message) {
        logger.log(Level.INFO, message);
    }
    public static void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }
    public static void logError(String message, Throwable e) {
        logger.log(Level.SEVERE, message, e);
    }
    public static void logDebug(String message) {
        logger.log(Level.FINE, message);
    }
}
public class Main {
    interface Multiplier {
        int multiply(int a, int b);
    }
    public static class BitMultiplier implements Multiplier {
        @Override
        public int multiply(int a, int b) {
            DebugHelper.logInfo("Вызван метод БитовоеУмножение с a=" + a + ", b=" + b);
            int result = 0;
            while (b > 0) {
                DebugHelper.logDebug("Текущее значение b: " + b);
                if ((b & 1) == 1) {
                    DebugHelper.logDebug("Битый младший бит b равен 1, добавляем a (" + a + ") к результату (" + result + ")");
                    result += a;
                }
                a <<= 1;
                b >>= 1;
                DebugHelper.logDebug("Сдвиг: a=" + a + ", b=" + b + ", промежуточный результат=" + result);
            }
            DebugHelper.logInfo("Результат Битового Умножения: " + result);
            return result;
        }
    }
    public static class ArrayMultiplier implements Multiplier {
        @Override
        public int multiply(int a, int b) {
            DebugHelper.logInfo("Вызван метод УмножениеМассивом с a=" + a + ", b=" + b);
            if (b < 0) {
                DebugHelper.logWarning("Множитель 'b' отрицательный (" + b + "), выброшено IllegalArgumentException");
                throw new IllegalArgumentException("Множитель 'b' не может быть отрицательным для этого метода.");
            }
            int result = a * b;
            DebugHelper.logInfo("Результат Умножения Массивом: " + result);
            return result;
        }
    }
    public static class RecursiveMultiplier implements Multiplier {
        @Override
        public int multiply(int a, int b) {
            DebugHelper.logInfo("Вызван метод РекурсивноеУмножение с a=" + a + ", b=" + b);
            if (b == 0) {
                DebugHelper.logDebug("Базовый случай: b=0, возвращаем 0");
                return 0;
            }
            if (b < 0) {
                DebugHelper.logDebug("b отрицательное (" + b + "), рекурсивный вызов с -b");
                return -multiply(a, -b);
            }
            int result = a + multiply(a, b - 1);
            DebugHelper.logDebug("Рекурсивный шаг: возвращаем " + a + " + РекурсивноеУмножение(" + a + ", " + (b - 1) + ") = " + result);
            return result;
        }
    }
    public static class LoopMultiplier implements Multiplier {
        @Override
        public int multiply(int a, int b) {
            DebugHelper.logInfo("Вызван метод УмножениеЦиклом с a=" + a + ", b=" + b);
            int result = 0;
            DebugHelper.logDebug("Начало цикла, b=" + b);
            for (int i = 0; i < b; i++) {
                DebugHelper.logDebug("Итерация " + i + ", добавляем a (" + a + ") к результату (" + result + ")");
                result += a;
            }
            DebugHelper.logInfo("Результат Умножения Циклом: " + result);
            return result;
        }
    }
    public static void main(String[] args) {
        DebugHelper.logInfo("Запуск программы");
        try (Scanner scanner = new Scanner(System.in)) {
            Map<Integer, Multiplier> multipliers = Map.of(
                    1, new BitMultiplier(),
                    2, new ArrayMultiplier(),
                    3, new RecursiveMultiplier(),
                    4, new LoopMultiplier()
            );
            System.out.println("Введите число a:");
            int a = scanner.nextInt();
            DebugHelper.logDebug("Введено число a: " + a);

            System.out.println("Введите число b:");
            int b = scanner.nextInt();
            DebugHelper.logDebug("Введено число b: " + b);

            System.out.println("Выберите способ умножения:");
            System.out.println("1. Битовое умножение");
            System.out.println("2. Умножение массивом");
            System.out.println("3. Рекурсивное умножение");
            System.out.println("4. Умножение циклом");

            int choice = scanner.nextInt();
            DebugHelper.logDebug("Выбран способ умножения: " + choice);
            if (multipliers.containsKey(choice)) {
                Multiplier multiplier = multipliers.get(choice);
                int result = multiplier.multiply(a, b);
                System.out.println("Результат: " + result);
                DebugHelper.logInfo("Операция умножения выполнена успешно, результат: " + result);
            } else {
                System.out.println("Некорректный выбор.");
                DebugHelper.logWarning("Некорректный выбор способа умножения: " + choice);
            }
        } catch (InputMismatchException e) {
            System.out.println("Ошибка: Введен некорректный формат числа.");
            DebugHelper.logError("Ошибка ввода: некорректный формат числа", e);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            DebugHelper.logError("Ошибка аргумента: " + e.getMessage(), e);
        } finally {
            DebugHelper.logInfo("Завершение работы программы");
        }
    }
}
/*

ВАРИАНТ С ОШИБКАМИ

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    // Ошибка компиляции: отсутствует закрывающая фигурная скобка для интерфейса
    interface Multiplier {
        int multiply(int a, int b);

    public static class BitMultiplier implements Multiplier {
        @Override
        public int multiply(int a, int b) {
            int result = 0;
            while (b > 0) {
                if ((b & 1) == 1) {
                    result += a;
                }
                a <<= 1;
                b >>= 1;
            }
            return result;
        }
    }

    public static class ArrayMultiplier implements Multiplier {
        @Override
        public int multiply(int a, int b) {
            // Логическая ошибка: не обрабатывается случай, когда a отрицательное
            if (b < 0) {
                throw new IllegalArgumentException("Множитель 'b' не может быть отрицательным для этого метода.");
            }
            return a * b;
        }
    }

    public static class RecursiveMultiplier implements Multiplier {
        @Override
        public int multiply(int a, int b) {
            // Ресурсная ошибка: возможен stack overflow при больших значениях b
            if (b == 0) {
                return 0;
            }
            if (b < 0) {
                return -multiply(a, -b);
            }
            return a + multiply(a, b - 1);
        }
    }

    public static class LoopMultiplier implements Multiplier {
        @Override
        public int multiply(int a, int b) {
            int result = 0;
            // Ошибка взаимодействия: цикл не работает для отрицательных b
            for (int i = 0; i < b; i++) {
                result += a;
            }
            return result;
        }
    }

    public static void main(String[] args) {
        // Синтаксическая ошибка: отсутствует точка с запятой
        try (Scanner scanner = new Scanner(System.in))
        {
            Map<Integer, Multiplier> multipliers = Map.of(
                    1, new BitMultiplier(),
                    2, new ArrayMultiplier(),
                    3, new RecursiveMultiplier(),
                    4, new LoopMultiplier()
            );
            System.out.println("Введите число a:");
            int a = scanner.nextInt();
            System.out.println("Введите число b:");
            int b = scanner.nextInt();

            System.out.println("Выберите способ умножения:");
            System.out.println("1. Битовое умножение");
            System.out.println("2. Умножение массивом");
            System.out.println("3. Рекурсивное умножение");
            System.out.println("4. Умножение циклом");

            int choice = scanner.nextInt();

            if (multipliers.containsKey(choice)) {  // Синтаксическая ошибка: лишняя закрывающая скобка
                Multiplier multiplier = multipliers.get(choice);
                int result = multiplier.multiply(a, b);
                System.out.println("Результат: " + result);
            } else {
                System.out.println("Некорректный выбор.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Ошибка: Введен некорректный формат числа.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

ЧИСТЫЙ ВАРИАНТ

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Main {


interface Multiplier {
    int multiply(int a, int b);
}

public static class BitMultiplier implements Multiplier {
    @Override
    public int multiply(int a, int b) {
        int result = 0;
        while (b > 0) {
            if ((b & 1) == 1) {
                result += a;
            }
            a <<= 1;
            b >>= 1;
        }
        return result;
    }
}

public static class ArrayMultiplier implements Multiplier {
    @Override
    public int multiply(int a, int b) {
        if (b < 0) {
            throw new IllegalArgumentException("Множитель 'b' не может быть отрицательным для этого метода.");
        }
        return a * b; // Более прямой способ без явного создания массива
    }
}

public static class RecursiveMultiplier implements Multiplier {
    @Override
    public int multiply(int a, int b) {
        if (b == 0) {
            return 0;
        }
        if (b < 0) {
            return -multiply(a, -b);
        }
        return a + multiply(a, b - 1);
    }
}

public static class LoopMultiplier implements Multiplier {
    @Override
    public int multiply(int a, int b) {
        int result = 0;
        for (int i = 0; i < b; i++) {
            result += a;
        }
        return result;
    }
}

public static void main(String[] args) {

    try (Scanner scanner = new Scanner(System.in)) {
        Map<Integer, Multiplier> multipliers = Map.of(
                1, new BitMultiplier(),
                2, new ArrayMultiplier(),
                3, new RecursiveMultiplier(),
                4, new LoopMultiplier()
        );
        System.out.println("Введите число a:");
        int a = scanner.nextInt();
        System.out.println("Введите число b:");
        int b = scanner.nextInt();

        System.out.println("Выберите способ умножения:");
        System.out.println("1. Битовое умножение");
        System.out.println("2. Умножение массивом");
        System.out.println("3. Рекурсивное умножение");
        System.out.println("4. Умножение циклом");

        int choice = scanner.nextInt();

        if (multipliers.containsKey(choice)) {
            Multiplier multiplier = multipliers.get(choice);
            int result = multiplier.multiply(a, b);
            System.out.println("Результат: " + result);
        } else {
            System.out.println("Некорректный выбор.");
        }

    } catch (InputMismatchException e) {
        System.out.println("Ошибка: Введен некорректный формат числа.");
    } catch (IllegalArgumentException e) {
        System.out.println("Ошибка: " + e.getMessage());
    }
}
}

 */