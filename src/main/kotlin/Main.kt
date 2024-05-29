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
    var flashDriveId by remember { mutableStateOf("") }
    var masterPassword by remember { mutableStateOf("") }

    Window(onCloseRequest = ::exitApplication, title = "Менеджер паролей") {
        when (currentScreen) {
            ScreenType.LOGIN -> {
                LoginScreen(
                    onLogin = { login, password ->
                        try {
                            val flashDriveInfo = getFlashDriveInfo()
                            if (flashDriveInfo == null) {
                                println("USB Flash Drive not detected")
                                return@LoginScreen
                            }
                            flashDriveId = flashDriveInfo.serialNumber

                            val user = userService.login(login, password, flashDriveId)
                            if (user != null) {
                                println("Login successful")
                                masterPassword = password
                                currentScreen = ScreenType.ACTION_SELECTION
                            } else {
                                println("User not found")
                            }
                        } catch (e: Exception) {
                            println("Login error: ${e.message}")
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
                        val flashDriveInfo = getFlashDriveInfo()
                        if (flashDriveInfo == null) {
                            println("USB Flash Drive not detected")
                            return@RegisterScreen
                        }
                        flashDriveId = flashDriveInfo.serialNumber

                        val success = userService.register(login, password, flashDriveId)
                        if (success) {
                            println("Registration successful")
                            currentScreen = ScreenType.LOGIN
                        } else {
                            println("Registration error")
                        }
                    },
                    onBack = {
                        currentScreen = ScreenType.LOGIN
                    }
                )
            }
            ScreenType.ACTION_SELECTION -> {
                println("Switching to ACTION_SELECTION")
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
                        val encryptedPassword = EncryptPasswordWithCryptoPro(password)
                        userService.addService(flashDriveId, service, encryptedPassword)
                        println("Service $service added with encrypted password")
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
                    println("Generated password: $generatedPassword")
                    val encryptedPassword = EncryptPasswordWithCryptoPro(generatedPassword)
                    userService.addService(flashDriveId, serviceName, encryptedPassword)
                    currentScreen = ScreenType.ACTION_SELECTION
                })
            }
            ScreenType.MANUAL_PASSWORD_ENTRY -> {
                ManualPasswordEntryScreen(onPasswordEntered = { enteredPassword ->
                    println("Entered password: $enteredPassword")
                    val encryptedPassword = EncryptPasswordWithCryptoPro(enteredPassword)
                    userService.addService(flashDriveId, serviceName, encryptedPassword)
                    currentScreen = ScreenType.ACTION_SELECTION
                })
            }
            ScreenType.VIEW_PASSWORD -> {
                PasswordViewScreen(
                    userId = flashDriveId,
                    userService = userService,
                    masterPassword = masterPassword,
                    onBack = {
                        currentScreen = ScreenType.ACTION_SELECTION
                    }
                )
            }
        }
    }
}
