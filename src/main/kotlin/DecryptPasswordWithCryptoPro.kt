import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.File

fun DecryptPasswordWithCryptoPro(encryptedPassword: String, masterPassword: String): String {
    // Создаем временный файл для зашифрованного пароля
    val encryptedFile = File.createTempFile("encryptedPassword", ".msg")
    encryptedFile.writeText(encryptedPassword)

    // Создаем временный файл для расшифрованного пароля
    val decryptedFile = File.createTempFile("decryptedPassword", ".txt")

    // Укажите путь к вашему скрипту и необходимые параметры
    val command = listOf("cmd.exe", "/c", "D:\\crypto\\cryptcp.x64", "-decr", "-dn", "Андреев Андрей Андреевич", "-pin", masterPassword, encryptedFile.absolutePath, decryptedFile.absolutePath)
    print(masterPassword)
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

    // Читаем расшифрованное содержимое
    val decryptedPassword = decryptedFile.readText()

    // Удаляем временные файлы
    encryptedFile.delete()
    decryptedFile.delete()

    return decryptedPassword
}
