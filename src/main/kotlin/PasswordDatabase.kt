interface PasswordDatabase {
    fun addService(userId: String, serviceName: String, encryptedPassword: String)
    fun getPassword(userId: String, serviceName: String): String?
    fun getServices(userId: String): List<String>
}
