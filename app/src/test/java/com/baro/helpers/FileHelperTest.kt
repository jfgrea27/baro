package com.baro.helpers

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import mockit.Expectations
import mockit.Mocked
import mockit.integration.junit4.JMockit
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.*
import java.nio.file.Paths
import java.util.*

@RunWith(JMockit::class)
class FileHelperTest {
    @Test
    fun getFileAtPathShouldReturnFileIfFileExists(@Mocked file: File?) {
        val pathToFile = Paths.get("path/to/parent", "filename")
        object : Expectations() {
            init {
                File(pathToFile.toString())
                file!!.exists()
                result = true
                file.absolutePath
                result = "path/to/parent/filename"
            }
        }
        val outputFile = FileHelper.getFileAtPath(pathToFile)
        Assert.assertEquals(outputFile?.absolutePath, "path/to/parent/filename")
    }

    @Test
    fun getFileAtPathShouldReturnNullIfFileDoesNotExists(@Mocked file: File?) {
        val pathToFile = Paths.get("path/to/nothing", "filename")
        object : Expectations() {
            init {
                File(pathToFile.toString())
                file!!.exists()
                result = false
            }
        }
        val outputFile = FileHelper.getFileAtPath(pathToFile)
        Assert.assertNull(outputFile)
    }

    @Test
    fun createFileAtPathShouldReturnFileAtPathIfFileExists(@Mocked file: File?) {
        val pathToFile = Paths.get("path/to/parent", "filename")
        object : Expectations() {
            init {
                File(pathToFile.toString())
                file!!.exists()
                result = true
                file.absolutePath
                result = "path/to/parent/filename"
            }
        }
        val outputFile = FileHelper.createFileAtPath(pathToFile)
        Assert.assertEquals(outputFile?.absolutePath, "path/to/parent/filename")
    }


    @Test
    @Throws(IOException::class)
    fun writeToFileShouldReturnTrueIfTextWasWrittenToValidFilePath(@Mocked fileWriter: FileWriter?) {
        val data = "Data"
        val file = File("valid/path/to/file")
        object : Expectations() {
            init {
                FileWriter(file)
                fileWriter!!.write(data)
                fileWriter!!.flush()
                fileWriter!!.close()
            }
        }
        val resultWritingFile = FileHelper.writeToFile(file, data)
        Assert.assertTrue(resultWritingFile)
    }

    @Test
    @Throws(IOException::class)
    fun writeToFileShouldReturnFalseIfTextWasNotWrittenToInvalidFilePath(@Mocked fileWriter: FileWriter?) {
        val data = "Data"
        val file = File("invalid/path/to/file")
        object : Expectations() {
            init {
                FileWriter(file)
                result = IOException()
            }
        }
        val resultWritingFile = FileHelper.writeToFile(file, data)
        Assert.assertFalse(resultWritingFile)
    }

    @Test
    @Throws(IOException::class)
    fun readFileShouldReturnFileContentWhenValidFilePath(@Mocked scanner: Scanner?) {
        val data = "Data"
        val file = File("valid/path/to/file")
        object : Expectations() {
            init {
                Scanner(file)
                scanner!!.useDelimiter("\\Z")
                result = scanner
                scanner!!.next()
                result = data
            }
        }
        Assert.assertEquals(FileHelper.readFile(file), "Data")
    }

    @Test
    @Throws(IOException::class)
    fun readFileShouldReturnNullWhenInvalidFilePath(@Mocked scanner: Scanner?) {
        val data = "Data"
        val file = File("invalid/path/to/file")
        object : Expectations() {
            init {
                Scanner(file)
                result = FileNotFoundException()
            }
        }
        Assert.assertNull(FileHelper.readFile(file))
    }

    @Test
    @Throws(FileNotFoundException::class)
    fun writeBitmapToFileShouldReturnTrueWhenBitmapIsSavedToValidFilePath(
            @Mocked fos: FileOutputStream?,
            @Mocked bitmap: Bitmap?) {
        val file = File("valid/path/to/file")
        object : Expectations() {
            init {
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
                result = true
            }
        }
        Assert.assertTrue(FileHelper.writeBitmapToFile(file, bitmap))
    }

    @Test
    @Throws(FileNotFoundException::class)
    fun writeBitmapToFileShouldReturnFalseWhenBitmapIsNotSavedToInvalidFilePath(
            @Mocked fos: FileOutputStream?,
            @Mocked bitmap: Bitmap?) {
        val file = File("invalid/path/to/file")
        object : Expectations() {
            init {
                FileOutputStream(file)
                result = FileNotFoundException()
            }
        }
        Assert.assertFalse(FileHelper.writeBitmapToFile(file, bitmap))
    }

    @Test
    fun writeUriToFileShouldReturnTrueWhenUriIsNotSavedToValidFilePath(
            @Mocked contentResolver: ContentResolver,
            @Mocked fis: FileInputStream) {

        // TODO complete this - cannot seem to mock effectively
    }

    @Test
    fun writeUriToFileShouldReturnTrueWhenUriIsSavedToInvalidFilePath(
            @Mocked contentResolver: ContentResolver,
            @Mocked fis: FileInputStream) {

        // TODO complete this - cannot seem to mock effectively
    }

    @Test
    @Throws(IOException::class)
    fun writeUriToFileShouldReturnFalseWhenMethodThrowsIOException(
            @Mocked contentResolver: ContentResolver,
            @Mocked fis: FileInputStream) {

        // TODO complete this - cannot seem to mock effectively
    }


}