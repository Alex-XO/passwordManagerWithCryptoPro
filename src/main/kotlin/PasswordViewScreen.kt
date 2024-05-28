import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PasswordViewScreen(userId: Int, userService: UserService, masterPassword: String, onBack: () -> Unit) {
    val services by remember { mutableStateOf(userService.getServices(userId)) }
    var selectedService by remember { mutableStateOf<String?>(null) }
    var decryptedPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Выберите сервис для просмотра пароля", style = MaterialTheme.typography.h6)

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(services) { service ->
                Button(
                    onClick = {
                        selectedService = service
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(service)
                }
            }
        }

        selectedService?.let {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Сервис: $it", style = MaterialTheme.typography.h6)
                Button(onClick = {
                    try {
                        decryptedPassword = userService.decryptPassword(userId, it, masterPassword)
                        showError = false
                    } catch (e: Exception) {
                        decryptedPassword = ""
                        showError = true
                    }
                }) {
                    Text("Расшифровать")
                }
                if (showError) {
                    Text("Ошибка расшифровки.", color = MaterialTheme.colors.error)
                } else if (decryptedPassword.isNotEmpty()) {
                    Text("Пароль: $decryptedPassword", style = MaterialTheme.typography.body1)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}
