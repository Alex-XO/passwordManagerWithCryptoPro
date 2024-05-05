import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.runtime.*

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Генератор паролей") {
        var showDialog by remember { mutableStateOf(true) }
        var generatePassword by remember { mutableStateOf(false) }
        var manualEntry by remember { mutableStateOf(false) }

        if (showDialog) {
            PasswordDialog(
                onGenerate = {
                    showDialog = false
                    generatePassword = true
                },
                onManualEntry = {
                    showDialog = false
                    manualEntry = true
                }
            )
        } else if (generatePassword) {
            PasswordGenerator()
        } else if (manualEntry) {
            // Здесь можно реализовать логику для ручного ввода пароля
        }
    }

    val command = listOf("cmd.exe", "/c", "D:\\crypto\\cryptcp.x64", "-encr", "-dn", "Андреев Андрей Андреевич", "D:\\crypto\\testAnd.txt", "D:\\crypto\\testAnd2.msg")
    val processBuilder = ProcessBuilder(command)
    processBuilder.redirectErrorStream(true) // Перенаправляем stderr в stdout для удобства чтения ошибок

    val process = processBuilder.start()
    val writer = OutputStreamWriter(process.outputStream)
    val reader = BufferedReader(InputStreamReader(process.inputStream))

    // Отдельный поток для чтения вывода процесса
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

    // Ожидаем завершения процесса
    val exitCode = process.waitFor()
    println("Процесс завершён с кодом: $exitCode")
}

