object Database {
    private val users = mutableMapOf<String, String>()

    fun createNewUser(login: String, passwordHash: String) {
        users[login] = passwordHash
    }

    fun getUser(login: String): String? = users[login]
}
