interface PasswordDatabase {
    fun createNewUser(username: String, hashedPassword: String, salt: String, flashDriveId: String): Boolean
    fun getUser(username: String): User?
    fun getServices(flashDriveId: String): List<String>
    fun getPassword(flashDriveId: String, serviceName: String): String?
    fun addService(flashDriveId: String, serviceName: String, encryptedPassword: String)
}

