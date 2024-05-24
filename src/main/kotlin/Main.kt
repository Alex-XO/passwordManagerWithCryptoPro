import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

enum class ScreenType {
    LOGIN,
    ACTION_SELECTION,
    ADD_SERVICE,
    GENERATE_PASSWORD,
    VIEW_PASSWORD,
    REGISTER
}

fun main() = application {
    val userService = UserService(MySQLDatabaseConnector())

    var currentScreen by remember { mutableStateOf(ScreenType.LOGIN) }

    Window(onCloseRequest = ::exitApplication, title = "Менеджер паролей") {
        when (currentScreen) {
            ScreenType.LOGIN -> {
                LoginScreen(
                    onLogin = { login, password ->
                        try {
                            val user = userService.login(login, password)
                            if (user != null) {
                                println("Вход выполнен успешно")
                                currentScreen = ScreenType.ACTION_SELECTION
                            } else {
                                println("Пользователь не найден")
                            }
                        } catch (e: Exception) {
                            println("Ошибка входа: ${e.message}")
                        }
                    },
                    onRegister = {
                        currentScreen = ScreenType.REGISTER
                    }
                )
            }
            ScreenType.REGISTER -> {
                RegisterScreen(
                    onRegister = { login, password ->
                        val success = userService.register(login, password)
                        if (success) {
                            println("Регистрация успешна")
                            currentScreen = ScreenType.LOGIN
                        } else {
                            println("Ошибка регистрации")
                        }
                    },
                    onBack = {
                        currentScreen = ScreenType.LOGIN
                    }
                )
            }
            ScreenType.ACTION_SELECTION -> {
                println("Переход в ACTION_SELECTION")
                ActionSelectionDialog(
                    onAddNewService = {
                        currentScreen = ScreenType.ADD_SERVICE
                    },
                    onViewPassword = {
                        currentScreen = ScreenType.VIEW_PASSWORD
                    }
                )
            }
            ScreenType.ADD_SERVICE -> {
                PasswordManagementScreen(onAddService = { serviceName, password ->
                    println("Сервис $serviceName добавлен с паролем $password")
                    currentScreen = ScreenType.GENERATE_PASSWORD
                })
            }
            ScreenType.GENERATE_PASSWORD -> {
                PasswordGenerator()
                currentScreen = ScreenType.ACTION_SELECTION
            }
            ScreenType.VIEW_PASSWORD -> {
                /*PasswordViewScreen(onBack = {
                    currentScreen = ScreenType.ACTION_SELECTION
                })

                 */
            }
        }
    }

    CertificateVerification()

}