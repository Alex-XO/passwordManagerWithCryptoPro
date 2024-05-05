import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog

class User(val id: Int,
           val login: String,
           val masterPasswordHash: String,
           val salt: ByteArray,
           var masterPassword: String = "")

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Генератор паролей") {
        var loggedIn by remember { mutableStateOf(false) }
        var actionSelectionDialogVisible by remember { mutableStateOf(false) }
        var showServiceScreen by remember { mutableStateOf(false) }
        var generatePassword by remember { mutableStateOf(false) }

        if (!loggedIn) {
            LoginScreen(onLoginSuccess = {
                loggedIn = true
                actionSelectionDialogVisible = true  // Показать диалог выбора действия
            })
        }

        if (actionSelectionDialogVisible) {
            ActionSelectionDialog(
                onAddNewService = {
                    actionSelectionDialogVisible = false
                    showServiceScreen = true  // Перейти к добавлению нового сервиса
                },
                onViewPassword = {
                    actionSelectionDialogVisible = false
                    // Перейти к просмотру пароля от сервиса, когда эта функция будет реализована
                }
            )
        }

        if (showServiceScreen) {
            PasswordManagementScreen(onAddService = { serviceName, password ->
                println("Сервис $serviceName добавлен с паролем $password")
                showServiceScreen = false
                generatePassword = true  // Активировать генератор паролей после добавления сервиса
            })
        }

        if (generatePassword) {
            PasswordGenerator()  // Запустить генератор паролей, если флаг активен
        }
    }

    // Запуск внешнего процесса с проверкой сертификата
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

@Composable
fun ActionSelectionDialog(onAddNewService: () -> Unit, onViewPassword: () -> Unit) {
    Dialog(onDismissRequest = { /* Обработка закрытия диалога */ }) {
        Card(modifier = Modifier.padding(16.dp), elevation = 8.dp) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally  // Выравнивание по центру
            ) {
                Text("Выберите действие:", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(10.dp))  // Добавляем немного пространства между текстом и кнопками
                Button(
                    onClick = onAddNewService,
                    modifier = Modifier.fillMaxWidth()  // Растягиваем на всю доступную ширину
                ) {
                    Text("Добавить новый сервис")
                }
                Button(
                    onClick = onViewPassword,
                    modifier = Modifier.fillMaxWidth()  // Растягиваем на всю доступную ширину
                ) {
                    Text("Вывести пароль от сервиса")
                }
            }
        }
    }
}

@Composable
fun PasswordManagementScreen(onAddService: (String, String) -> Unit) {
    var serviceName by remember { mutableStateOf("") }
    var showPasswordDialog by remember { mutableStateOf(false) }
    val maxWidth = 360.dp  // Максимальная ширина для элементов

    Dialog(onDismissRequest = { /* Обработка закрытия диалога */ }) {
        Card(modifier = Modifier.padding(16.dp).width(maxWidth), elevation = 8.dp) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Введите название сервиса:", style = MaterialTheme.typography.h6)
                TextField(
                    value = serviceName,
                    onValueChange = { serviceName = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()  // Используем всю доступную ширину карточки
                )
                Button(
                    onClick = { showPasswordDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Добавить новый сервис")
                }

                if (showPasswordDialog) {
                    PasswordDialog(
                        onGenerate = {
                            // Генерация пароля и добавление сервиса
                            onAddService(serviceName, "Сгенерированный пароль")
                            showPasswordDialog = false
                        },
                        onManualEntry = {
                            // Ввод пароля вручную и добавление сервиса
                            onAddService(serviceName, "Введенный пользователем пароль")
                            showPasswordDialog = false
                        }
                    )
                }
            }
        }
    }
}
