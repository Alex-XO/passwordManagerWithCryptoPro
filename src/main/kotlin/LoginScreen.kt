import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { /* Обработка закрытия диалога */ }) {
        Card(modifier = Modifier.width(300.dp).padding(16.dp), elevation = 8.dp) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Введите логин:", style = MaterialTheme.typography.h6)
                TextField(
                    value = login,
                    onValueChange = { login = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = MaterialTheme.colors.primary, // Цвет линии при фокусе
                        unfocusedIndicatorColor = Color.Gray // Цвет линии без фокуса
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                Text("Введите мастер-пароль:", style = MaterialTheme.typography.h6)
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = MaterialTheme.colors.primary,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        // Вызов функции входа или других действий при нажатии "Done" на клавиатуре
                    })
                )
                Button(
                    /* onClick = {
                        val hashedPassword = PasswordHasher.hashPassword(password)
                        val userPasswordHash = Database.getUser(login)
                        if (userPasswordHash == null || hashedPassword != userPasswordHash) {
                            showErrorDialog = true
                        } else {
                            onLoginSuccess()
                        }
                    }, */
                    onClick = onLoginSuccess, // Всегда успешный преход на следующий шаг
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Войти")
                }
            }
        }
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Ошибка") },
                text = { Text("Неправильный логин или пароль") },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
