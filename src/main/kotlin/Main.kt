import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.runtime.*

enum class ScreenType {
    LOGIN,
    ACTION_SELECTION,
    ADD_SERVICE,
    VIEW_PASSWORD,
    GENERATE_PASSWORD
}

class User(val id: Int,
           val login: String,
           val masterPasswordHash: String,
           val salt: ByteArray,
           var masterPassword: String = "")

fun main() = application {
    val userService = UserService(MySQLDatabaseConnector())

    var currentScreen by remember { mutableStateOf(ScreenType.LOGIN) }

    Window(onCloseRequest = ::exitApplication, title = "Менеджер паролей") {

        when (currentScreen) {
            ScreenType.LOGIN -> {
                LoginScreen(onLogin = { login, password ->
                    val user = userService.login(login, password)
                    if (user != null) {
                        currentScreen = ScreenType.ACTION_SELECTION
                    } else {
                        // Обработать ошибку входа в систему (например, показать сообщение об ошибке)
                    }
                })
            }

            ScreenType.ACTION_SELECTION -> {
                ActionSelectionDialog(
                    onAddNewService = {
                        currentScreen = ScreenType.ADD_SERVICE
                    },
                    onViewPassword = {
                        // РЕАЛИЗОВАТЬ: Перейти к просмотру пароля от сервиса
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

            ScreenType.VIEW_PASSWORD -> {
                // Реализjdfnm экран для просмотра паролей от сервиса
                // После завершения можно вернуть в ACTION_SELECTION
                currentScreen = ScreenType.ACTION_SELECTION
            }

            ScreenType.GENERATE_PASSWORD -> {
                PasswordGenerator()  // Запустить генератор паролей, если флаг активен
                currentScreen = ScreenType.ACTION_SELECTION
            }
        }

        /*
        if (currentScreen == ScreenType.LOGIN) {
            LoginScreen(onLogin = { login, password ->
                val user = userService.login(login, password)
                currentScreen = ScreenType.ACTION_SELECTION
            })
        }


        if (currentScreen == ScreenType.ACTION_SELECTION) {
            ActionSelectionDialog(
                onAddNewService = {
                    currentScreen = ScreenType.ADD_SERVICE
                },
                onViewPassword = {
                    // РЕАЛИЗОВАТЬ: Перейти к просмотру пароля от сервиса
                }
            )
        }

        if (currentScreen == ScreenType.ADD_SERVICE) {
            PasswordManagementScreen(onAddService = { serviceName, password ->
                println("Сервис $serviceName добавлен с паролем $password")
                currentScreen = ScreenType.GENERATE_PASSWORD
            })
        }

        if (currentScreen == ScreenType.GENERATE_PASSWORD) {
            PasswordGenerator()  // Запустить генератор паролей, если флаг активен
        }

        */
    }

    // Запуск внешнего процесса: проверка сертификата
    CertificateVerification()
}