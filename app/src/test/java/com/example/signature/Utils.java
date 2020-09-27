package com.example.signature;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


class Utils {

    /**
     * @param fileName
     * @return
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String read(String fileName) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * @param fileName
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] readBytes(String fileName) {
        byte[] content = null;
        try {
            content = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

//    static String readFileAsString(String path) throws URISyntaxException {
//        return new String(readFileAsBytes(path), StandardCharsets.US_ASCII);
//    }
//
//    static byte[] readFileAsBytes(String path) throws URISyntaxException {
//        return read(ClassLoader.getSystemClassLoader().getResource(path).toURI().getPath());
//    }
//
//    private static byte[] read(String path) {
//        try {
//            RandomAccessFile f = new RandomAccessFile(path, "r");
//            if (f.length() > Integer.MAX_VALUE)
//                throw new RuntimeException("File is too large");
//            byte[] b = new byte[(int) f.length()];
//            f.readFully(b);
//            if (f.getFilePointer() != f.length())
//                throw new RuntimeException("File length changed while reading");
//            return b;
//        } catch (IOException e) {
//            throw new RuntimeException("Could not read file");
//        }
//    }
}
