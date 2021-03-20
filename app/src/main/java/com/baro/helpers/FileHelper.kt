package com.baro.helpers

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

import java.io.*
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

object FileHelper {
    /**
     * This method returns a File Object if a file exists at the given Path object Path.
     * @param path This is a Path Object of the candidate file.
     * @return File This returns a File Object only if a file exists at that path; null otherwise
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getFileAtPath(path: Path?): File? {
        val file = File(path.toString())
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

    /**
     * This method returns a File Object for file at path, creating the file and
     * its file system path if it does not already exist.
     * @param path This is a Path Object of the candidate file.
     * @return File This returns a File Object for the file at path; if fails to create,
     * returns null
     */
    fun createFileAtPath(path: Path?): File? {
        val file = File(path.toString())
        return if (file.exists()) {
            file
        } else {
            createFile(file)
        }
    }

    private fun createFile(file: File?): File? {
        file?.parentFile?.mkdirs()
        try {
            file?.createNewFile()
        } catch (e: IOException) {
            return null
        }
        return file
    }

    /**
     * This method creates a directory at path/to/parentDirectory/directoryName.
     * @param parentDirectory This is a File object of the parent directory.
     * @param directoryName This is a String object of the desired directory name.
     * @return boolean This returns true only if a directory at path/to/parentDirectory/directoryName.
     * was successfully created/already exists.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createDirectory(parentDirectory: File?, directoryName: String?): Boolean {
        val directoryPath = Paths.get(parentDirectory?.absolutePath, directoryName)
        val directoryFile = File(directoryPath.toString())
        return if (!directoryFile.exists()) {
            directoryFile.mkdirs()
        } else true
    }

    /**
     * This method writes data to file.
     * @param file This is the desire file the data will be written to.
     * @param data This is the data to be written to the file.
     * @return boolean This returns true only if the data was successfully written to the file;
     * false otherwise
     */
    fun writeToFile(file: File?, data: String?): Boolean {
        try {
            val writer = FileWriter(file)
            writer.write(data)
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * This  method reads data from file.
     * @param file This is the desired file to be read.
     * @return String This returns a String of the file' qs content, returning null if an error was
     * caught
     */
    fun readFile(file: File?): String? {
        return try {
            val scanner = Scanner(file)
            scanner.useDelimiter("\\Z").next()
        } catch (e: FileNotFoundException) {
            null
        }
    }

    /**
     * This  method writes a Bitmap object to a file.
     * @param file This is the desired file where the bitmap will be saved
     * @param bitmap This is the desired Bitmap to write to file.
     * @return boolean This returns true if the Bitmap was appropriately written to the file; false
     * otherwise
     */
    fun writeBitmapToFile(file: File?, bitmap: Bitmap?): Boolean {
        if (bitmap != null) {
            return try {
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            } catch (e: FileNotFoundException) {
                false
            }
        }
        return false
    }

    fun writeUriToFile(destination: File?, uri: Uri?, content: ContentResolver?): File? {
        val file = createFile(destination)
        var `in`: InputStream? = null
        `in` = try {
            uri?.let { content?.openInputStream(it) }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
        return try {
            saveInputStream(`in`, file?.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @Throws(IOException::class)
    private fun saveInputStream(`in`: InputStream?, desiredLocation: String?): File? {
        val outputFile = File(desiredLocation)
        val fos = FileOutputStream(outputFile)
        copyStream(`in`, fos)
        return outputFile
    }

    @Throws(IOException::class)
    private fun copyStream(input: InputStream?, output: OutputStream?) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input?.read(buffer).also { bytesRead = it!! } != -1) {
            output?.write(buffer, 0, bytesRead)
        }
    }

    fun deleteFile(file: File?) {
        if (file?.isDirectory == true) {
            for (child in file.listFiles()) deleteFile(child)
        }
        file?.delete()
    }
}