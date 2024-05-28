import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.random.Random

@Composable
fun PasswordGenerator(onGenerateComplete: (String) -> Unit) {
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var length by remember { mutableStateOf(TextFieldValue("12")) }
    val useSpecial = remember { mutableStateOf(false) }
    val useDigits = remember { mutableStateOf(false) }
    val useUppercase = remember { mutableStateOf(false) }
    val useLowercase = remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Text("Параметры генерации:", style = MaterialTheme.typography.h6)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), elevation = 8.dp) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Option("Использовать специальные символы", useSpecial)
                        Option("Использовать цифры", useDigits)
                        Option("Использовать заглавные буквы", useUppercase)
                        Option("Использовать строчные буквы", useLowercase)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Длина пароля:", fontSize = 16.sp, modifier = Modifier.width(150.dp))
                            BasicTextField(
                                value = length.text,
                                onValueChange = { if (it.all { char -> char.isDigit() }) length = TextFieldValue(it) },
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                                modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(4.dp)).padding(4.dp)
                            )
                        }
                        Button(onClick = {
                            val generatedPassword = generatePassword(useSpecial.value, useDigits.value, useUppercase.value, useLowercase.value, length.text.toInt())
                            password = TextFieldValue(generatedPassword)
                            onGenerateComplete(generatedPassword) // Возвращаем сгенерированный пароль
                        }, modifier = Modifier.align(Alignment.End)) {
                            Text("Сгенерировать")
                        }
                    }
                }
                Card(modifier = Modifier.fillMaxWidth().height(100.dp), shape = RoundedCornerShape(8.dp), elevation = 8.dp) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Сгенерированный пароль:", style = MaterialTheme.typography.body1)
                        BasicTextField(
                            value = password.text,
                            onValueChange = { password = TextFieldValue(it) },
                            readOnly = true,
                            textStyle = LocalTextStyle.current.copy(color = Color.Black),
                            modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray, RoundedCornerShape(4.dp)).padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Option(text: String, state: MutableState<Boolean>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = state.value, onCheckedChange = { state.value = it })
        Text(text)
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
