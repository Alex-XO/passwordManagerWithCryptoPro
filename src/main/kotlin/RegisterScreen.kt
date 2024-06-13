import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun RegisterScreen(onRegister: (String, String) -> Unit, onBack: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { /* Обработка закрытия диалога */ }) {
        Card(modifier = Modifier.width(300.dp).padding(16.dp), elevation = 8.dp) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Регистрация", style = MaterialTheme.typography.h6)
                TextField(
                    value = login,
                    onValueChange = { login = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Логин") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedIndicatorColor = MaterialTheme.colors.primary,
                        unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    label = { Text("Мастер-пароль") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedIndicatorColor = MaterialTheme.colors.primary,
                        unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                )
                Button(
                    onClick = {
                        if (login.isNotBlank() && password.isNotBlank()) {
                            onRegister(login, password)
                        } else {
                            errorMessage = "Логин и пароль не могут быть пустыми"
                            showErrorDialog = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Зарегистрироваться")
                }
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Назад")
                }
            }
        }
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Ошибка") },
                text = { Text(errorMessage) },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}