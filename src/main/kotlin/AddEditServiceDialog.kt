import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.random.Random

@Composable
fun AddEditServiceDialog(
    service: Service?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(service?.name ?: "") }
    var password by remember { mutableStateOf(service?.password ?: "") }
    var showPassword by remember { mutableStateOf(false) }
    var showPasswordGenerator by remember { mutableStateOf(false) }

    if (showPasswordGenerator) {
        PasswordGenerator(
            onGenerateComplete = {
                password = it
                showPasswordGenerator = false
            },
            onBack = { showPasswordGenerator = false }
        )
    } else {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Название сервиса") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            cursorColor = MaterialTheme.colors.primary,
                            focusedIndicatorColor = MaterialTheme.colors.primary,
                            unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Пароль") },
                            singleLine = true,
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = MaterialTheme.colors.primary,
                                focusedIndicatorColor = MaterialTheme.colors.primary,
                                unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                            )
                        )
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showPassword) "Скрыть пароль" else "Показать пароль"
                            )
                        }
                    }
                    Button(
                        onClick = { showPasswordGenerator = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text("Сгенерировать пароль")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Отмена")
                        }
                        Button(
                            onClick = { onSave(name, password) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                            shape = RoundedCornerShape(20.dp),
                        ) {
                            Text("Сохранить", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
