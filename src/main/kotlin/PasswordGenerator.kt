import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.random.Random

@Composable
fun PasswordGenerator(
    onGenerateComplete: (String) -> Unit,
    onBack: () -> Unit
) {
    var useSpecial by remember { mutableStateOf(true) }
    var useDigits by remember { mutableStateOf(true) }
    var useUppercase by remember { mutableStateOf(true) }
    var useLowercase by remember { mutableStateOf(true) }
    var length by remember { mutableStateOf(12) }

    Dialog(onDismissRequest = onBack) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Настройки генерации пароля", style = MaterialTheme.typography.h6)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(checked = useSpecial, onCheckedChange = { useSpecial = it })
                    Text("Специальные символы")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(checked = useDigits, onCheckedChange = { useDigits = it })
                    Text("Цифры")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(checked = useUppercase, onCheckedChange = { useUppercase = it })
                    Text("Прописные буквы")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(checked = useLowercase, onCheckedChange = { useLowercase = it })
                    Text("Строчные буквы")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Длина пароля:")
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = length.toFloat(),
                        onValueChange = { length = it.toInt() },
                        valueRange = 8f..32f,
                        steps = 24,
                        modifier = Modifier.weight(1f) // Расширяем Slider, чтобы он занимал доступное пространство
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(length.toString())
                }
                Button(
                    onClick = {
                        val generatedPassword = generatePassword(useSpecial, useDigits, useUppercase, useLowercase, length)
                        onGenerateComplete(generatedPassword)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сгенерировать")
                }
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Назад")
                }
            }
        }
    }
}

fun generatePassword(useSpecial: Boolean, useDigits: Boolean, useUppercase: Boolean, useLowercase: Boolean, length: Int): String {
    val charPool = mutableListOf<Char>()
    if (useSpecial) charPool.addAll("!@#$%^&*()_+-=[]{}|;:,.<>?".toList())
    if (useDigits) charPool.addAll('0'..'9')
    if (useUppercase) charPool.addAll('A'..'Z')
    if (useLowercase) charPool.addAll('a'..'z')

    return (1..length).map { charPool.random(Random) }.joinToString("")
}
