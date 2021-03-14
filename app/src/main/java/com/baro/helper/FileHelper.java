package com.baro.helper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileHelper {

    /**
     * This method creates a directory at path/to/parentDirectory/directoryName.
     * @param parentDirectory This is a File object of the parent directory.
     * @param directoryName This is a String object of the desired directory name.
     * @return boolean This returns true only if a directory at path/to/parentDirectory/directoryName.
     * was successfully created/already exists.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean createDirectory(File parentDirectory, String directoryName) {
        Path directoryPath = Paths.get(parentDirectory.getAbsolutePath(), directoryName);

        File directoryFile = new File(String.valueOf(directoryPath));

        if(!directoryFile.exists()) {
            return directoryFile.mkdirs();
        }
        return  true;
    }


    /**
     * This method creates a file at path/to/parentDirectory/fileName.
     * @param parentDirectory This is a File object of the parent directory.
     * @param fileName This is a String object of the desired file name.
     * @return boolean This returns true only if a file at path/to/parentDirectory/directoryName.
     * was successfully created/already exists.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean createFile(File parentDirectory, String fileName) {
        Path filePath = Paths.get(parentDirectory.getAbsolutePath(), fileName);

        File fileFile = new File(String.valueOf(filePath));

        if(!fileFile.exists()) {
            return fileFile.mkdir();
        }
        return  true;
    }

    /**
     * This method writes data to file.
     * @param file This is a File where the data is intended to be written.
     * @param data This is the data to be written to the File.
     * @return boolean This returns true only if the data was successfully written to the file.
     */
    public static boolean writeToFile(File file, String data) {

        try {
            return writeToFile(new FileWriter(file), data);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * This  method writes data to file.
     * @param writer This is a FileWriter, used to write to a file.
     * @param data This is the data to be written via the FileWriter.
     * @return boolean This returns true only if the data was successfully written to the file.
     */
    public static boolean writeToFile(FileWriter writer, String data) {
        try {
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    public static String readFile(File file){


        try {
            return readFile(new Scanner(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String readFile(Scanner reader) {
        String output = "";

        while (reader.hasNextLine()) {
            output += reader.nextLine();
        }
        reader.close();

        return output;

    }


    public static boolean writeBitmapToFile(File file, String text) {

        return false;
    }



    public static boolean deleteFile(File file) {
        return false;
    }


}
