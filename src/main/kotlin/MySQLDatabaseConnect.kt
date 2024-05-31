import io.github.cdimascio.dotenv.Dotenv
import java.security.SecureRandom
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.*

class MySQLDatabaseConnector : PasswordDatabase {
    private val connection: Connection
    private val statement: Statement

    init {
        val dotenv = Dotenv.load()
        val dbPassword = dotenv["DB_PASSWORD"]
        val dbUser = dotenv["DB_USER"]
        val dbURL = dotenv["DB_URL"]

        connection = DriverManager.getConnection(dbURL, dbUser, dbPassword)
        statement = connection.createStatement()
    }

    override fun createNewUser(login: String, masterPasswordHash: String, salt: String, flashDriveId: String): Boolean {
        val sqlInsertUser = "INSERT INTO users (login, master_password_hash, salt, flash_drive_id) VALUES (?, ?, ?, ?)"

        val preparedStatement = connection.prepareStatement(sqlInsertUser)
        preparedStatement.setString(1, login)
        preparedStatement.setString(2, masterPasswordHash)
        preparedStatement.setString(3, salt)
        preparedStatement.setString(4, flashDriveId)

        preparedStatement.executeUpdate()
        println("User created successfully: login = $login, flashDriveId = $flashDriveId")
        return true
    }

    override fun getUser(login: String): User? {
        val sql = "SELECT login, master_password_hash, salt, flash_drive_id FROM users WHERE login = ?"
        val preparedStatement = connection.prepareStatement(sql)
        preparedStatement.setString(1, login)
        val result = preparedStatement.executeQuery()
        if (result.next()) {
            val masterPasswordHash = result.getString("master_password_hash")
            val login = result.getString("login")
            val salt = result.getString("salt")
            val flashDriveId = result.getString("flash_drive_id")
            println("Retrieved user: login = $login, flashDriveId = $flashDriveId")
            return User(login, masterPasswordHash, salt, flashDriveId)
        }
        return null
    }


    override fun addService(flashDriveId: String, serviceName: String, encryptedPassword: String) {
        val sqlInsertService = "INSERT INTO passwords (flash_drive_id, service_name, password) VALUES (?, ?, ?)"
        val preparedStatement = connection.prepareStatement(sqlInsertService)
        preparedStatement.setString(1, flashDriveId)
        preparedStatement.setString(2, serviceName)
        preparedStatement.setString(3, encryptedPassword)

        preparedStatement.executeUpdate()
        println("Service added: flashDriveId = $flashDriveId, serviceName = $serviceName")
    }

    override fun getPassword(flashDriveId: String, serviceName: String): String? {
        val sqlSelectPassword = "SELECT password FROM passwords WHERE flash_drive_id = ? AND service_name = ?"
        val preparedStatement = connection.prepareStatement(sqlSelectPassword)
        preparedStatement.setString(1, flashDriveId)
        preparedStatement.setString(2, serviceName)
        val result = preparedStatement.executeQuery()
        if (result.next()) {
            return result.getString("password")
        }
        return null
    }

    override fun getServices(flashDriveId: String): List<String> {
        val sqlSelectServices = "SELECT service_name FROM passwords WHERE flash_drive_id = ?"
        val preparedStatement = connection.prepareStatement(sqlSelectServices)
        preparedStatement.setString(1, flashDriveId)
        val result = preparedStatement.executeQuery()
        val services = mutableListOf<String>()
        while (result.next()) {
            services.add(result.getString("service_name"))
        }
        return services
    }
}
