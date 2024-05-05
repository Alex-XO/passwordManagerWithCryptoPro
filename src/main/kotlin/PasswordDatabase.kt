interface PasswordDatabase {
    fun addService(userId: Int, serviceName: String, encryptedPassword: String)
    fun getPassword(userId: Int, serviceName: String): String?
}