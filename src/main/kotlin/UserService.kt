import java.security.SecureRandom

class UserService(
    private val dataBase: MySQLDatabaseConnector
) {
    fun login(username: String, password: String): User? {
        val user = dataBase.getUser(username)
        return if (user != null) {
            val hashedPassword = PasswordHasher.hashPassword(password + user.salt.decodeToString())
            println("Input password hash: $hashedPassword")
            println("Stored password hash: ${user.masterPasswordHash}")
            if (user.masterPasswordHash == hashedPassword) {
                user
            } else {
                throw Exception("Wrong password")
            }
        } else {
            throw Exception("User not found")
        }
    }

    fun register(login: String, password: String): Boolean {
        return try {
            val salt = generateSalt()
            val hashedPassword = PasswordHasher.hashPassword(password + salt.decodeToString())
            println("Registering user with hash: $hashedPassword")
            dataBase.createNewUser(login, hashedPassword, salt)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun addService(userId: Int, serviceName: String, encryptedPassword: String) {
        dataBase.addService(userId, serviceName, encryptedPassword)
    }

    fun getServices(userId: Int): List<String> {
        return dataBase.getServices(userId)
    }

    fun decryptPassword(userId: Int, serviceName: String, masterPassword: String): String {
        val encryptedPassword = dataBase.getPassword(userId, serviceName) ?: return "Пароль не найден"
        return DecryptPasswordWithCryptoPro(encryptedPassword, masterPassword)
    }

    private fun generateSalt(size: Int = 16): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(size)
        random.nextBytes(salt)
        return salt
    }
}
