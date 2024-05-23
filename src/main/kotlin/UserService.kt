class UserService(
    private val dataBase: MySQLDatabaseConnector
) {
    fun login(username: String, password: String): User? {
        val user = dataBase.getUser(username) ?: throw Exception("User not found")
        val newHash = PasswordHasher.hashPassword(password)
        if (user.masterPasswordHash != newHash) {
            throw Exception("Wrong password")
        }

        return user
    }
}