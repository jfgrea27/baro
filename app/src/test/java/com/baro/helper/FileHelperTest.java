package com.baro.helper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileHelperTest {

    @Test
    public void createDirectoryShouldReturnTrueIfDirectoryIsCreated() {
        File parent = new File("path/to/parent/directory");
        String directoryName = "Directory Name";

        assertTrue(FileHelper.createDirectory(parent, directoryName));
    }

    @Test
    public void createFileShouldReturnTrueIfFileIsCreated() {
        File parent = new File("path/to/parent/directory");
        String fileName = "File Name";

        assertTrue(FileHelper.createFile(parent, fileName));
    }


    @Test
    public void writeToFileShouldReturnTrueIfTextWasWrittenToFile() throws IOException {
        FileWriter mockWriter = Mockito.mock(FileWriter.class);
        String data = "Data";

        assertTrue(FileHelper.writeToFile(mockWriter, data));

        verify(mockWriter).write(data);
    }

    @Test
    public void writeToFileShouldCloseFileWriter() throws IOException {
        FileWriter mockWriter = Mockito.mock(FileWriter.class);
        String data = "Data";

        FileHelper.writeToFile(mockWriter, data);

        verify(mockWriter).close();
    }



    @Test
    public void readFileShouldReturnFileContent() throws IOException {
        Scanner mockScanner = Mockito.mock(Scanner.class);

        when(mockScanner.hasNextLine()).thenReturn(true, false);
        when(mockScanner.nextLine()).thenReturn("data");

        assertEquals(FileHelper.readFile(mockScanner), "data");
    }


    @Test
    public void readFileShouldCloseScanner() throws IOException {
        Scanner mockScanner = Mockito.mock(Scanner.class);

        when(mockScanner.hasNextLine()).thenReturn(false);
        FileHelper.readFile(mockScanner);

        verify(mockScanner).close();
    }



}
