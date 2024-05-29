import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun PasswordManagementScreen(onAddService: (String, String) -> Unit, onGeneratePassword: (String) -> Unit, onEnterPasswordManually: (String) -> Unit) {
    var serviceName by remember { mutableStateOf("") }
    var showPasswordDialog by remember { mutableStateOf(false) }
    val maxWidth = 360.dp

    Dialog(onDismissRequest = { /* Обработка закрытия диалога */ }) {
        Card(modifier = Modifier.padding(16.dp).width(maxWidth), elevation = 8.dp) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Введите название сервиса:", style = MaterialTheme.typography.h6)
                TextField(
                    value = serviceName,
                    onValueChange = { serviceName = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { showPasswordDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Добавить новый сервис")
                }

                if (showPasswordDialog) {
                    PasswordDialog(
                        onGenerate = {
                            onGeneratePassword(serviceName)
                            showPasswordDialog = false
                        },
                        onManualEntry = {
                            onEnterPasswordManually(serviceName)
                            showPasswordDialog = false
                        }
                    )
                }
            }
        }
    }
}
