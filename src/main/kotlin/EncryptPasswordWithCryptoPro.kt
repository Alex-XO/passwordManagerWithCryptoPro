import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.File

fun EncryptPasswordWithCryptoPro(password: String, certificatePath: String): String {
    // Создаем временный файл для пароля
    val inputFile = File.createTempFile("password", ".txt")
    inputFile.writeText(password)

    // Создаем временный файл для зашифрованного пароля
    val outputFile = File.createTempFile("encryptedPassword", ".msg")

    // полный путь к сертификату и необходимые параметры
    val command = listOf("cmd.exe", "/c", "D:\\crypto\\cryptcp.x64", "-encr", "-f", certificatePath, inputFile.absolutePath, outputFile.absolutePath)
    val processBuilder = ProcessBuilder(command)
    processBuilder.redirectErrorStream(true)

    val process = processBuilder.start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))

    val thread = Thread {
        try {
            reader.lines().forEach { line ->
                println(line)
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
