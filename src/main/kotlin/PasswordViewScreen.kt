import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun PasswordViewScreen(
    userId: String,
    userService: UserService,
    masterPassword: String,
    certificatePath: String,
    onBack: () -> Unit
) {
    var selectedService by remember { mutableStateOf<String?>(null) }
    var decryptedPassword by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val services = userService.getServices(userId)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Выберите сервис для просмотра пароля", style = MaterialTheme.typography.h6)
        services.forEach { service ->
            Button(
                onClick = { selectedService = service },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(service)
            }
        }

        selectedService?.let { service ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Сервис: $service", style = MaterialTheme.typography.h6)
                Button(
                    onClick = {
                        try {
                            val encryptedPassword = userService.getPassword(userId, service)
                            if (encryptedPassword != null) {
                                decryptedPassword = DecryptPasswordWithCryptoPro(encryptedPassword, masterPassword, certificatePath)
                            } else {
                                errorMessage = "Пароль не найден"
                                showError = true
                            }
                        } catch (e: Exception) {
                            errorMessage = "Ошибка расшифровки: ${e.message}"
                            showError = true
                        }
                    }
                ) {
                    Text("Расшифровать")
                }
                decryptedPassword?.let {
                    Text("Пароль: $it", style = MaterialTheme.typography.h6)
                }
                if (showError) {
                    Text(errorMessage, color = Color.Red)
                }
                Button(onClick = onBack) {
                    Text("Назад")
                }
            }
        }
    }
}
