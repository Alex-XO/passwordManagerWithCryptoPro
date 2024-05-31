import java.security.SecureRandom
import java.util.Base64

class UserService(private val dataBase: PasswordDatabase) {

    fun register(username: String, password: String, flashDriveId: String): Boolean {
        val salt = generateSalt()
        val hashedPassword = PasswordHasher.hashPassword(password + salt)
        println("Registering user: username = $username, flashDriveId = $flashDriveId")
        return try {
            dataBase.createNewUser(username, hashedPassword, salt, flashDriveId)
            true
        } catch (e: Exception) {
            println("Registration error: ${e.message}")
            false
        }
    }



    fun login(username: String, password: String, flashDriveId: String): User? {
        val user = dataBase.getUser(username) ?: return null
        val inputHash = PasswordHasher.hashPassword(password + user.salt)
        println("Logging in user: $username")
        println("Input password hash: $inputHash")
        println("Stored password hash: ${user.masterPasswordHash}")
        println("Input flash drive ID: $flashDriveId")
        println("Stored flash drive ID: ${user.flashDriveId}")

        if (user.masterPasswordHash == inputHash && user.flashDriveId == flashDriveId) {
            return user
        }
        throw Exception("Wrong password or flash drive ID does not match")
    }

    fun getServices(flashDriveId: String): List<String> {
        return dataBase.getServices(flashDriveId)
    }

    fun getPassword(flashDriveId: String, serviceName: String): String? {
        return dataBase.getPassword(flashDriveId, serviceName)
    }

    fun addService(flashDriveId: String, serviceName: String, encryptedPassword: String) {
        dataBase.addService(flashDriveId, serviceName, encryptedPassword)
    }

    fun decryptPassword(flashDriveId: String, serviceName: String, masterPassword: String, certificatePath: String): String {
        val encryptedPassword = getPassword(flashDriveId, serviceName) ?: return "Пароль не найден"
        return DecryptPasswordWithCryptoPro(encryptedPassword, masterPassword, certificatePath)
    }

    private fun generateSalt(size: Int = 16): String {
        val random = SecureRandom()
        val salt = ByteArray(size)
        random.nextBytes(salt)
        return salt.joinToString("") { String.format("%02x", it) }
    }
}
