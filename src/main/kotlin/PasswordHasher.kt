import java.security.MessageDigest

object PasswordHasher {
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA3-256")
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hash.toHex()
    }

    /*
    fun hashPasswordWithSHA256(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hash.toHex()
    }

    fun hashPasswordWithSHA3(password: String): String {
        val digest = MessageDigest.getInstance("SHA3-256")
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hash.toHex()
    }
    */

    private fun ByteArray.toHex(): String {
        return joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}