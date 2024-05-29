import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinNT.HANDLE
import com.sun.jna.ptr.IntByReference
import java.io.File

data class FlashDriveInfo(val driveLetter: String, val serialNumber: String)

fun getFlashDriveInfo(): FlashDriveInfo? {
    val roots = File.listRoots()
    for (root in roots) {
        if (isUSB(root)) {
            val driveLetter = root.path
            val volumeSerialNumber = getVolumeSerialNumber(driveLetter)
            if (volumeSerialNumber != null) {
                return FlashDriveInfo(driveLetter, volumeSerialNumber)
                println(driveLetter)
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
