data class User(
    val login: String,
    val masterPasswordHash: String,
    val salt: String,
    val flashDriveId: String
)
