package info.sul_game.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File

object FileUtil {
    fun createImageFile(context: Context, extension: String = ".jpg"): File {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val validExtension = when (extension.lowercase()) {
            ".jpg", ".jpeg", ".png" -> extension
            else -> ".jpg"
        }
        return File.createTempFile(
            "IMG_${System.currentTimeMillis()}_", /* 파일 이름 */
            validExtension,
            storageDir
        )
    }

    fun createVideoFile(context: Context, extension: String = ".mp4"): File {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        val validExtension = when (extension.lowercase()) {
            ".mp4", ".avi", ".mov" -> extension
            else -> ".mp4"
        }
        return File.createTempFile(
            "VIDEO_${System.currentTimeMillis()}_", /* 파일 이름 */
            validExtension, /* 파일 확장자 */
            storageDir /* 파일 저장 위치 */
        )
    }

    fun createAudioFile(context: Context, extension: String = ".mp3"): File {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val validExtension = when (extension.lowercase()) {
            ".mp3", ".wav", ".aac" -> extension
            else -> ".mp3"
        }
        return File.createTempFile(
            "AUDIO_${System.currentTimeMillis()}_", /* 파일 이름 */
            validExtension, /* 파일 확장자 */
            storageDir /* 파일 저장 위치 */
        )
    }

    fun createTempFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = when (getFileExtension(context, uri)) {
            "mp4", "avi", "mov" -> createVideoFile(context, ".${getFileExtension(context, uri)}")
            "mp3", "wav", "aac" -> createAudioFile(context, ".${getFileExtension(context, uri)}")
            else -> createImageFile(context, ".${getFileExtension(context, uri)}")
        }
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private fun getFileExtension(context: Context, uri: Uri): String {
        val mimeType = context.contentResolver.getType(uri)
        return when {
            mimeType?.startsWith("video/") == true -> mimeType.substringAfterLast("/")
            mimeType?.startsWith("audio/") == true -> mimeType.substringAfterLast("/")
            mimeType?.startsWith("image/") == true -> mimeType.substringAfterLast("/")
            else -> ""
        }
    }
}
