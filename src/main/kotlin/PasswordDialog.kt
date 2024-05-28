import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PasswordDialog(onGenerate: () -> Unit, onManualEntry: () -> Unit) {
    Dialog(onDismissRequest = { /* Обработать закрытие диалога */ }) {
        Card(modifier = Modifier.padding(16.dp), elevation = 8.dp) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Вы хотите сгенерировать надежный пароль или написать сами?", style = MaterialTheme.typography.body1)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = onGenerate) {
                        Text("Сгенерировать")
                    }
                    Button(onClick = onManualEntry) {
                        Text("Написать вручную")
                    }
                }
            }
        }
    }
}

