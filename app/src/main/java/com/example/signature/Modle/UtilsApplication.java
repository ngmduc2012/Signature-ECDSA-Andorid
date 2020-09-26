package com.example.signature.Modle;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.example.signature.MyApplication.getContext;

public class UtilsApplication {
    // get name file form path
    public static String nameFile(String path) {
        String name = null;
        for (String i : path.split("/")) {
            name = i;
        }
        return name;
    }

    // delete private key file in storage
    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
        Toast.makeText(getContext(), "Deleted Private Key File", Toast.LENGTH_SHORT).show();
        if (file.exists()) {
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (file.exists()) {
                getContext().getApplicationContext().deleteFile(file.getName());
            }
        }
    }

}
