import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun CertificateVerification() {
    val command = listOf("cmd.exe", "/c", "D:\\crypto\\cryptcp.x64", "-encr", "-dn", "Андреев Андрей Андреевич", "D:\\crypto\\testAnd.txt", "D:\\crypto\\testAnd2.msg")
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
}