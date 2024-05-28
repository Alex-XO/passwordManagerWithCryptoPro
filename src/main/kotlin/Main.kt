import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

enum class ScreenType {
    LOGIN,
    ACTION_SELECTION,
    ADD_SERVICE,
    GENERATE_PASSWORD,
    VIEW_PASSWORD,
    REGISTER,
    MANUAL_PASSWORD_ENTRY
}

fun main() = application {
    val userService = UserService(MySQLDatabaseConnector())

    var currentScreen by remember { mutableStateOf(ScreenType.LOGIN) }
    var serviceName by remember { mutableStateOf("") }
    var servicePassword by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(0) }
    var masterPassword by remember { mutableStateOf("") }

    Window(onCloseRequest = ::exitApplication, title = "Менеджер паролей") {
        when (currentScreen) {
            ScreenType.LOGIN -> {
                LoginScreen(
                    onLogin = { login, password ->
                        try {
                            val user = userService.login(login, password)
                            if (user != null) {
                                println("Вход выполнен успешно")
                                userId = user.id
                                masterPassword = password
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
                PasswordManagementScreen(
                    onAddService = { service, password ->
                        val encryptedPassword = EncryptPasswordWhisCryptoPro(password)
                        userService.addService(userId, service, encryptedPassword)
                        println("Сервис $service добавлен с зашифрованным паролем")
                        currentScreen = ScreenType.ACTION_SELECTION
                    },
                    onGeneratePassword = {
                        serviceName = it
                        currentScreen = ScreenType.GENERATE_PASSWORD
                    },
                    onEnterPasswordManually = {
                        serviceName = it
                        currentScreen = ScreenType.MANUAL_PASSWORD_ENTRY
                    }
                )
            }
            ScreenType.GENERATE_PASSWORD -> {
                PasswordGenerator(onGenerateComplete = { generatedPassword ->
                    println("Сгенерированный пароль: $generatedPassword")
                    val encryptedPassword = EncryptPasswordWhisCryptoPro(generatedPassword)
                    userService.addService(userId, serviceName, encryptedPassword)
                    currentScreen = ScreenType.ACTION_SELECTION
                })
            }
            ScreenType.MANUAL_PASSWORD_ENTRY -> {
                ManualPasswordEntryScreen(onPasswordEntered = { enteredPassword ->
                    println("Введенный пароль: $enteredPassword")
                    val encryptedPassword = EncryptPasswordWhisCryptoPro(enteredPassword)
                    userService.addService(userId, serviceName, encryptedPassword)
                    currentScreen = ScreenType.ACTION_SELECTION
                })
            }
            ScreenType.VIEW_PASSWORD -> {
                PasswordViewScreen(userId = userId, userService = userService, masterPassword = masterPassword, onBack = {
                    currentScreen = ScreenType.ACTION_SELECTION
                })
            }
        }
    }
}
