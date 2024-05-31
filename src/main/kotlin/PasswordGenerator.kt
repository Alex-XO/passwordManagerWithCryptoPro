import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Настройки генерации пароля", style = MaterialTheme.typography.h6)
        Row {
            Checkbox(checked = useSpecial, onCheckedChange = { useSpecial = it })
            Text("Специальные символы")
        }
        Row {
            Checkbox(checked = useDigits, onCheckedChange = { useDigits = it })
            Text("Цифры")
        }
        Row {
            Checkbox(checked = useUppercase, onCheckedChange = { useUppercase = it })
            Text("Прописные буквы")
        }
        Row {
            Checkbox(checked = useLowercase, onCheckedChange = { useLowercase = it })
            Text("Строчные буквы")
        }
        Row {
            Text("Длина пароля:")
            Spacer(modifier = Modifier.width(8.dp))
            Slider(value = length.toFloat(), onValueChange = { length = it.toInt() }, valueRange = 8f..32f, steps = 24)
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

fun generatePassword(useSpecial: Boolean, useDigits: Boolean, useUppercase: Boolean, useLowercase: Boolean, length: Int): String {
    val charPool = mutableListOf<Char>()
    if (useSpecial) charPool.addAll("!@#$%^&*()_+-=[]{}|;:,.<>?".toList())
    if (useDigits) charPool.addAll('0'..'9')
    if (useUppercase) charPool.addAll('A'..'Z')
    if (useLowercase) charPool.addAll('a'..'z')

    return (1..length).map { charPool.random(Random) }.joinToString("")
}
