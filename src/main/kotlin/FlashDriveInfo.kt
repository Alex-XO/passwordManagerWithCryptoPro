import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinNT.HANDLE
import com.sun.jna.ptr.IntByReference
import java.io.File

data class FlashDriveInfo(val driveLetter: String, val serialNumber: String, val certificatePath: String?)

fun getFlashDriveInfo(): FlashDriveInfo? {
    val roots = File.listRoots()
    for (root in roots) {
        if (isUSB(root)) {
            val driveLetter = root.path
            val volumeSerialNumber = getVolumeSerialNumber(driveLetter)
            val certificatePath = findCertificateOnFlashDrive(driveLetter)
            if (volumeSerialNumber != null) {
                println("Detected USB flash drive with serial number: $volumeSerialNumber, drive letter: $driveLetter, certificate path: $certificatePath")
                return FlashDriveInfo(driveLetter, volumeSerialNumber, certificatePath)
            }
        }
    }
    return null
}

fun isUSB(root: File): Boolean {
    val type = getDriveType(root.path)
    return type == 2 // DRIVE_REMOVABLE
}

fun getDriveType(path: String): Int {
    return Kernel32.INSTANCE.GetDriveType(path)
}

fun getVolumeSerialNumber(driveLetter: String): String? {
    val volumeNameBuffer = CharArray(256)
    val fileSystemNameBuffer = CharArray(256)
    val volumeSerialNumber = IntByReference()

    val result = Kernel32.INSTANCE.GetVolumeInformation(
        driveLetter,
        volumeNameBuffer,
        volumeNameBuffer.size,
        volumeSerialNumber,
        null,
        null,
        fileSystemNameBuffer,
        fileSystemNameBuffer.size
    )

    return if (result) {
        Integer.toHexString(volumeSerialNumber.value)
    } else {
        null
    }
}

fun findCertificateOnFlashDrive(driveLetter: String): String? {
    val root = File(driveLetter)
    val files = root.listFiles() ?: return null

    for (file in files) {
        if (file.isFile && (file.extension == "p7b" || file.extension == "cer")) {
            return file.absolutePath
        }
    }
    return null
}
