import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.File

fun EncryptPasswordWithCryptoPro(password: String): String {
    // Создаем временный файл для пароля
    val inputFile = File.createTempFile("password", ".txt")
    inputFile.writeText(password)

    // Создаем временный файл для зашифрованного пароля
    val outputFile = File.createTempFile("encryptedPassword", ".msg")

    // Укажите путь к вашему скрипту и необходимые параметры
    val command = listOf("cmd.exe", "/c", "D:\\crypto\\cryptcp.x64", "-encr", "-dn", "Андреев Андрей Андреевич", inputFile.absolutePath, outputFile.absolutePath)
    val processBuilder = ProcessBuilder(command)
    processBuilder.redirectErrorStream(true)

    val process = processBuilder.start()
    val writer = OutputStreamWriter(process.outputStream)
    val reader = BufferedReader(InputStreamReader(process.inputStream))

    val thread = Thread {
        try {
            reader.lines().forEach { line ->
                writer.write("Y\n")
                writer.flush()
                if (line.contains("Вы хотите использовать этот сертификат (Да[Y], Нет[N], Отмена[C])?")) {
                    writer.write("Y\n")
                    writer.flush()
                }
            }
        } catch (e: Exception) {
            println("Ошибка при чтении вывода: ${e.message}")
        }
    }
    thread.start()

    val exitCode = process.waitFor()
    println("Процесс завершён с кодом: $exitCode")

    // Читаем зашифрованное содержимое
    val encryptedPassword = outputFile.readText()

    // Удаляем временные файлы
    inputFile.delete()
    outputFile.delete()

    return encryptedPassword
}
