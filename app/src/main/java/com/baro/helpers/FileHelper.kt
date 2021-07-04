package com.baro.helpers

import android.R.attr.path
import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.*
import java.nio.file.Path
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.collections.ArrayList


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

    fun createDirAtPath(path: Path?): File {
        val file = File(path.toString())
        if (!file.exists()) {
            file.mkdirs()
        }

        return file
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

    private fun saveInputStream(`in`: InputStream?, desiredLocation: String?): Boolean {
        val outputFile = File(desiredLocation)
        return try {
            val fos = FileOutputStream(outputFile)
            copyStream(`in`, fos)
        } catch (e: IOException) {
            false
        }
    }

    private fun copyStream(input: InputStream?, output: OutputStream?): Boolean {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        return try {
            while (input?.read(buffer).also { bytesRead = it!! } != -1) {
                output?.write(buffer, 0, bytesRead)
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    fun deleteFile(file: File?) {
        if (file?.isDirectory == true) {
            file.deleteRecursively()
        }
        file?.delete()
    }


    fun copyVideoToFile(outputAddress: File, originalUri: Uri, content: ContentResolver?): File? {
        var `in`: InputStream? = null
        `in` = try {
            content?.openInputStream(originalUri)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
        return try {
            copyFile(`in`, outputAddress)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @Throws(IOException::class)
    fun copyFile(`in`: InputStream?, outputFile: File?): File? {
        val fos = FileOutputStream(outputFile)
        copyStream(`in`, fos)
        return outputFile
    }


    fun compressDirectory(directory: File, zipFile: File): File {
        val fileList = getFileList(directory)

        try {
            FileOutputStream(zipFile).use { fos ->
                ZipOutputStream(fos).use { zos ->
                    for (filePath in fileList) {
                        println("Compressing: $filePath")

                        // Creates a zip entry.
                        val name = filePath.substring(
                            directory.absolutePath.length + 1,
                            filePath.length
                        )
                        val zipEntry = ZipEntry(name)
                        zos.putNextEntry(zipEntry)

                        // Read file content and write to zip output stream.
                        try {
                            FileInputStream(filePath).use { fis ->
                                val buffer = ByteArray(1024)
                                var length: Int
                                while (fis.read(buffer).also { length = it } > 0) {
                                    zos.write(buffer, 0, length)
                                }

                                // Close the zip entry.
                                zos.closeEntry()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("Cannot write to file", e.toString())

                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("Cannot create FOS", e.toString())
        }
        return zipFile
    }

    /**
     * Get files list from the directory recursive to the sub directory.
     */
    private fun getFileList(directory: File): ArrayList<String> {
        val fileList = ArrayList<String>()
        val files = directory.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.isFile) {
                    fileList.add(file.absolutePath)
                } else {
                    val subFiles = getFileList(file)
                    for (subFile in subFiles) {
                        fileList.add(subFile)
                    }
                }
            }
        }
        return fileList
    }

    @Throws(IOException::class)
    fun unzip(zipFilePath: File, destDirectory: String): Boolean {
        val destDir = File(destDirectory)
        if (!destDir.exists()) {
            destDir.mkdirs()
        }
        ZipFile(zipFilePath).use { zip ->

            zip.entries().asSequence().forEach { entry ->

                zip.getInputStream(entry).use { input ->


                    val filePath = destDirectory + File.separator + entry.name

                    if (!entry.isDirectory) {
                        createFile(File(filePath))
                        extractFile(input, filePath)
                    } else {

                        val dir = File(filePath)
                        dir.mkdirs()
                    }

                }

            }
            deleteFile(zipFilePath)
            return true
        }

    }

    @Throws(IOException::class)
    private fun extractFile(inputStream: InputStream, destFilePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(destFilePath))
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read: Int
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }

    /**
     * Size of the buffer to read/write data
     */
    private const val BUFFER_SIZE = 4096

}