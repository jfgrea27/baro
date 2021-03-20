package com.baro.helpers;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileHelper {


    /**
     * This method returns a File Object if a file exists at the given Path object Path.
     * @param path This is a Path Object of the candidate file.
     * @return File This returns a File Object only if a file exists at that path; null otherwise
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static File getFileAtPath(Path path) {

        File file = new File(path.toString());

        if(file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * This method returns a File Object for file at path, creating the file and
     * its file system path if it does not already exist.
     * @param path This is a Path Object of the candidate file.
     * @return File This returns a File Object for the file at path; if fails to create,
     * returns null
     */
    public static File createFileAtPath(Path path) {
        File file = new File(path.toString());

        if(file.exists()) {
            return file;
        } else {
            return createFile(file);
        }
    }

    private static File createFile(File file) {
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            return  null;
        }
        return file;
    }

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
     * This method writes data to file.
     * @param file This is the desire file the data will be written to.
     * @param data This is the data to be written to the file.
     * @return boolean This returns true only if the data was successfully written to the file;
     * false otherwise
     */
    public static boolean writeToFile(File file, String data) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    /**
     * This  method reads data from file.
     * @param file This is the desired file to be read.
     * @return String This returns a String of the file' qs content, returning null if an error was
     * caught
     */
    public static String readFile(File file) {

        try {
            Scanner scanner = new Scanner(file);
            return scanner.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * This  method writes a Bitmap object to a file.
     * @param file This is the desired file where the bitmap will be saved
     * @param bitmap This is the desired Bitmap to write to file.
     * @return boolean This returns true if the Bitmap was appropriately written to the file; false
     * otherwise
     */
    public static boolean writeBitmapToFile(File file, Bitmap bitmap) {

        try {
            FileOutputStream fos = new FileOutputStream(file);
            return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            return  false;
        }
    }


    public static boolean deleteFile(File file) {
        return false;
    }


}
