import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.runtime.*

class User(val id: Int,
           val login: String,
           val masterPasswordHash: String,
           val salt: ByteArray,
           var masterPassword: String = "")

fun main() = application {
    val userService = UserService(MySQLDatabaseConnector())

    Window(onCloseRequest = ::exitApplication, title = "Менеджер паролей") {
        var loggedIn by remember { mutableStateOf(false) }
        var actionSelectionDialogVisible by remember { mutableStateOf(false) }
        var showServiceScreen by remember { mutableStateOf(false) }
        var generatePassword by remember { mutableStateOf(false) }

        if (!loggedIn) {
            LoginScreen(onLogin = { login, password ->
                val user = userService.login(login, password)
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
                    // РЕАЛИЗОВАТЬ: Перейти к просмотру пароля от сервиса
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

    // Запуск внешнего процесса: проверка сертификата
    //CertificateVerification()
}

