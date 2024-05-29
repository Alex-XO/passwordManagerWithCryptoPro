import java.security.SecureRandom
import java.util.Base64

class UserService(
    private val dataBase: MySQLDatabaseConnector
) {
    fun login(username: String, password: String, flashDriveId: String): User? {
        val user = dataBase.getUser(username)
        println("User found: $user")
        return if (user != null) {
            val hashedPassword = PasswordHasher.hashPassword(password + user.salt)
            println("Input password hash: $hashedPassword")
            println("Stored password hash: ${user.masterPasswordHash}")
            println("Input flash drive ID: $flashDriveId")
            println("Stored flash drive ID: ${user.flashDriveId}")
            if (user.masterPasswordHash == hashedPassword && user.flashDriveId == flashDriveId) {
                user
            } else {
                throw Exception("Wrong password or flash drive ID does not match")
            }
        } else {
            throw Exception("User not found")
        }
    }

    fun register(username: String, password: String, flashDriveId: String): Boolean {
        val salt = generateSalt()
        val hashedPassword = PasswordHasher.hashPassword(password + salt)
        return try {
            dataBase.createNewUser(username, hashedPassword, salt, flashDriveId)
            true
        } catch (e: Exception) {
            println("Registration error: ${e.message}")
            false
        }
    }

    fun addService(flashDriveId: String, serviceName: String, encryptedPassword: String) {
        dataBase.addService(flashDriveId, serviceName, encryptedPassword)
    }

    fun getPassword(flashDriveId: String, serviceName: String): String? {
        return dataBase.getPassword(flashDriveId, serviceName)
    }

    fun getServices(flashDriveId: String): List<String> {
        return dataBase.getServices(flashDriveId)
    }

    fun decryptPassword(flashDriveId: String, serviceName: String): String {
        val encryptedPassword = getPassword(flashDriveId, serviceName) ?: return "Пароль не найден"
        return DecryptPasswordWithCryptoPro(encryptedPassword, "")
    }

    private fun generateSalt(size: Int = 16): String {
        val random = SecureRandom()
        val salt = ByteArray(size)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }
}
