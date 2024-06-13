import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

data class Service(
    val name: String,
    val password: String
)

@Composable
fun ServiceListScreen() {
    var services by remember { mutableStateOf(listOf<Service>()) }
    var showDialog by remember { mutableStateOf(false) }
    var currentService by remember { mutableStateOf<Service?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp)
    ) {
        if (services.isEmpty()) {
            Button(
                onClick = {
                    currentService = null
                    isEditing = false
                    showDialog = true
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            ) {
                Text("Добавить сервис", color = Color.White)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Список сервисов",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(services) { service ->
                        ServiceItem(
                            service = service,
                            onInfo = {
                                currentService = it
                                isEditing = true
                                showDialog = true
                            }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = {
                        currentService = null
                        isEditing = false
                        showDialog = true
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Добавить сервис", color = Color.White)
                }
            }
        }
    }

    if (showDialog) {
        AddEditServiceDialog(
            service = currentService,
            onDismiss = { showDialog = false },
            onSave = { name, password ->
                if (isEditing && currentService != null) {
                    services = services.map {
                        if (it == currentService) Service(name, password) else it
                    }
                } else {
                    services = services + Service(name, password)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun ServiceItem(service: Service, onInfo: (Service) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
                    .height(40.dp)
                ) {
                Icon(
                    imageVector = Icons.Default.Lock, // Иконка замочка слева
                    contentDescription = "Service Icon",
                    modifier = Modifier.size(40.dp).padding(end = 16.dp)
                )
                Column {
                    Text(service.name, style = MaterialTheme.typography.subtitle1)
                }
            }
            IconButton(onClick = { onInfo(service) }) {
                Icon(Icons.Default.Info, contentDescription = "Информация")
            }
        }
    }
}
