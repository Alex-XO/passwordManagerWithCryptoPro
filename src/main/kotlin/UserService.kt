import io.github.cdimascio.dotenv.Dotenv
import java.security.SecureRandom
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.*

class UserService(
    private val dataBase: MySQLDatabaseConnector
) {
    fun login(username: String, password: String): User? {
        val user = dataBase.getUser(username)
        return if (user != null) {
            val hashedPassword = PasswordHasher.hashPassword(password + user.salt.decodeToString())
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
            dataBase.createNewUser(login, hashedPassword, salt)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun generateSalt(size: Int = 16): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(size)
        random.nextBytes(salt)
        return salt
    }
}