package com.baro.helper;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
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
     * @param writer This is a FileWriter, used to write to a file.
     * @param data This is the data to be written via the FileWriter.
     * @return boolean This returns true only if the data was successfully written to the file;
     * false otherwise
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


    /**
     * This  method reads data in file.
     * @param reader This is a Scanner used to read file.
     * @return String This returns a String of the file' qs content, returning null if an error was
     * caught
     */
    public static String readFile(Scanner reader) {
        return reader.useDelimiter("\\Z").next();
    }


    /**
     * This  method writes a Bitmap object to a file's FileOutputStream.
     * @param fos This is a file's FileOutputStream.
     * @param bitmap This is the desired Bitmap to write to file.
     * @return boolean This returns true if the Bitmap was appropriately written to the file; false
     * otherwise
     */
    public static boolean writeBitmapToFile(FileOutputStream fos, Bitmap bitmap) {

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }



    public static boolean deleteFile(File file) {
        return false;
    }


}
