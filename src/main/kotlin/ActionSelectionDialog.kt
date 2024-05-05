import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ActionSelectionDialog(onAddNewService: () -> Unit, onViewPassword: () -> Unit) {
    Dialog(onDismissRequest = { /* Обработка закрытия диалога */ }) {
        Card(modifier = Modifier.padding(16.dp), elevation = 8.dp) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally  // Выравнивание по центру
            ) {
                Text("Выберите действие:", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(10.dp))  // Добавляем немного пространства между текстом и кнопками
                Button(
                    onClick = onAddNewService,
                    modifier = Modifier.fillMaxWidth()  // Растягиваем на всю доступную ширину
                ) {
                    Text("Добавить новый сервис")
                }
                Button(
                    onClick = onViewPassword,
                    modifier = Modifier.fillMaxWidth()  // Растягиваем на всю доступную ширину
                ) {
                    Text("Вывести пароль от сервиса")
                }
            }
        }
    }
}