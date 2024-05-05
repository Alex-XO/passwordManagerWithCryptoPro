import io.github.cdimascio.dotenv.Dotenv
import java.awt.desktop.UserSessionListener
import java.security.SecureRandom
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.*

class MySQLDatabaseConnector: PasswordDatabase {
    val statement: Statement
    val connection: Connection
    constructor() {
        val dotenv = Dotenv.load()
        val dbPassword = dotenv["DB_PASSWORD"]
        val dbUser = dotenv["DB_USER"]
        val dbURL = dotenv["DB_URL"]

        connection = DriverManager.getConnection(dbURL, dbUser, dbPassword)
        statement = connection.createStatement()
    }

    fun createNewUser(login: String, masterPasswordHash: String) {
        val salt = generateSalt()
        val saltAsString = Base64.getEncoder().encodeToString(salt)
        val sqlInsertUser = "INSERT INTO users (login, master_password_hash, salt) VALUES (?, ?, ?)"

        val preparedStatement = connection.prepareStatement(sqlInsertUser)
        preparedStatement.setString(1, login)
        preparedStatement.setString(2, masterPasswordHash)
        preparedStatement.setString(3, saltAsString)

        preparedStatement.executeUpdate()
        println("Пользователь успешно создан.")
    }

    private fun generateSalt(size: Int = 16): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(size)
        random.nextBytes(salt)
        return salt
    }

    fun initTables() {
        val sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                login TEXT NOT NULL,
                master_password_hash TEXT NOT NULL,
                salt TEXT NOT NULL
            )
        """.trimIndent()

        statement.execute(sql)

        val tableSQL = """
            CREATE TABLE IF NOT EXISTS passwords (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                service_name VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
        """.trimIndent()

        statement.execute(tableSQL)
        println("Таблица успешно создана или уже существует.")
    }

    fun getUser(login: String): User? {
        val sql = "SELECT id, login, master_password_hash, salt FROM users WHERE login = ?"
        val preparedStatement = connection.prepareStatement(sql)
        preparedStatement.setString(1, login)
        val result = preparedStatement.executeQuery()
        if (result.next()) {
            val masterPasswordHash = result.getString("master_password_hash")
            val id = result.getInt("id")
            val login = result.getString("login")
            val salt = result.getString("salt")
            val saltByteArray = Base64.getDecoder().decode(salt)
            return User(id, login, masterPasswordHash, saltByteArray)
        }
        return null
    }

    override fun addService(userId: Int, serviceName: String, encryptedPassword: String) {
        val sqlInsertService = "INSERT INTO passwords (user_id, service_name, password) VALUES (?, ?, ?)"
        val preparedStatement = connection.prepareStatement(sqlInsertService)
        preparedStatement.setInt(1, userId)
        preparedStatement.setString(2, serviceName)
        preparedStatement.setString(3, encryptedPassword)

        preparedStatement.executeUpdate()
        println("Сервис сохранен в базу данных.")
    }

    override fun getPassword(userId: Int, serviceName: String): String? {
        val sqlSelectPassword = "SELECT password FROM passwords WHERE user_id = ? AND service_name = ?"
        val preparedStatement = connection.prepareStatement(sqlSelectPassword)
        preparedStatement.setInt(1, userId)
        preparedStatement.setString(2, serviceName)
        val result = preparedStatement.executeQuery()
        if (result.next()) {
            val password = result.getString("password")
            return password
        }
        return null
    }
}