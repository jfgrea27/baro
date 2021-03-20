package com.baro.helpers;

import android.graphics.Bitmap;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import mockit.Capturing;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class FileHelperTest {


    @Test
    public void getFileAtPathShouldReturnFileIfFileExists(@Mocked File file) {
        Path pathToFile = Paths.get("path/to/parent", "filename");

        new Expectations() {{
            new File(pathToFile.toString());
            file.exists(); result = true;
            file.getAbsolutePath(); result = "path/to/parent/filename";
        }};


        File outputFile = FileHelper.getFileAtPath(pathToFile);
        assertEquals(outputFile.getAbsolutePath(), "path/to/parent/filename");
    }

    @Test
    public void getFileAtPathShouldReturnNullIfFileDoesNotExists(@Mocked File file) {
        Path pathToFile = Paths.get("path/to/nothing", "filename");

        new Expectations() {{
            new File(pathToFile.toString());
            file.exists(); result = false;
        }};

        File outputFile = FileHelper.getFileAtPath(pathToFile);
        assertNull(outputFile);
    }

    @Test
    public void createFileAtPathShouldReturnFileAtPathIfFileExists(@Mocked File file) {
        Path pathToFile = Paths.get("path/to/parent", "filename");

        new Expectations() {{
            new File(pathToFile.toString());
            file.exists(); result = true;
            file.getAbsolutePath(); result = "path/to/parent/filename";
        }};

        File outputFile = FileHelper.createFileAtPath(pathToFile);
        assertEquals(outputFile.getAbsolutePath(), "path/to/parent/filename");
    }


    @Test
    public void createDirectoryShouldReturnTrueIfDirectoryIsCreated() {
        File parent = new File("path/to/parent/directory");
        String directoryName = "Directory Name";

        assertTrue(FileHelper.createDirectory(parent, directoryName));
    }




    @Test
    public void writeToFileShouldReturnTrueIfTextWasWrittenToValidFilePath(@Mocked FileWriter fileWriter) throws IOException {
        String data = "Data";
        File file = new File("valid/path/to/file");

        new Expectations() {{
            new FileWriter(file);

            fileWriter.write(data);
            fileWriter.flush();
            fileWriter.close();
        }};

        boolean resultWritingFile = FileHelper.writeToFile(file, data);
        assertTrue(resultWritingFile);

    }

    @Test
    public void writeToFileShouldReturnFalseIfTextWasNotWrittenToInvalidFilePath(@Mocked FileWriter fileWriter) throws IOException {
        String data = "Data";
        File file = new File("invalid/path/to/file");

        new Expectations() {{
            new FileWriter(file); result = new IOException();
        }};

        boolean resultWritingFile = FileHelper.writeToFile(file, data);
        assertFalse(resultWritingFile);

    }

    @Test
    public void readFileShouldReturnFileContentWhenValidFilePath(@Mocked Scanner scanner) throws IOException {
        String data = "Data";
        File file = new File("valid/path/to/file");

        new Expectations() {{
            new Scanner(file);

            scanner.useDelimiter("\\Z"); result = scanner;
            scanner.next(); result = data;
        }};


        assertEquals(FileHelper.readFile(file), "Data");
    }


    @Test
    public void readFileShouldReturnNullWhenInvalidFilePath(@Mocked Scanner scanner) throws IOException {
        String data = "Data";
        File file = new File("invalid/path/to/file");

        new Expectations() {{
            new Scanner(file); result = new FileNotFoundException();
        }};

        assertNull(FileHelper.readFile(file));
    }


    @Test
    public void writeBitmapToFileShouldReturnTrueWhenBitmapIsSavedToValidFilePath(
            @Mocked FileOutputStream fos,
            @Mocked Bitmap bitmap) throws FileNotFoundException {

        File file = new File("valid/path/to/file");

        new Expectations() {{
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file)); result = true;
        }};
        assertTrue(FileHelper.writeBitmapToFile(file, bitmap));
    }

    @Test
    public void writeBitmapToFileShouldReturnFalseWhenBitmapIsNotSavedToInvalidFilePath(
            @Mocked FileOutputStream fos,
            @Mocked Bitmap bitmap) throws FileNotFoundException {

        File file = new File("invalid/path/to/file");

        new Expectations() {{
            new FileOutputStream(file); result = new FileNotFoundException();
        }};

        assertFalse(FileHelper.writeBitmapToFile(file, bitmap));
    }
}
