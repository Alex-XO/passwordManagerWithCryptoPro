data class User(
    val id: Int,
    val login: String,
    val masterPasswordHash: String,
    val salt: ByteArray
)
