import java.io.File

fun main() {
    val numbers = File("input.txt").readText().split(Regex("\\s+")).map { it.toInt() }

    // Находим минимальное ПОЛОЖИТЕЛЬНОЕ трёхзначное число, делящееся на 7
    val min3DigitDiv7 = numbers.filter { it in 100..999 && it % 7 == 0 }.minOrNull()
        ?: throw Error("Не найдено трёхзначного числа, делящегося на 7")

    // Находим минимальное четырёхзначное число (по абсолютному значению)
    val min4Digit = numbers.filter { kotlin.math.abs(it) in 1000..9999 }.minByOrNull { kotlin.math.abs(it) }
        ?: throw Error("Не найдено четырёхзначного числа")

    val lastDigit = kotlin.math.abs(min4Digit) % 10
    var count = 0
    var maxSum = Int.MIN_VALUE

    for (i in 0..<numbers.size - 1) {
        val a = numbers[i]
        val b = numbers[i + 1]

        // Условие: хотя бы одно число меньше min3DigitDiv7 (105)
        if (a < min3DigitDiv7 || b < min3DigitDiv7) {
            // Проверяем последнюю цифру произведения
            if (kotlin.math.abs(a * b) % 10 == lastDigit) {
                count++
                maxSum = maxOf(maxSum, a + b)
            }
        }
    }

    println("$count $maxSum")  
}