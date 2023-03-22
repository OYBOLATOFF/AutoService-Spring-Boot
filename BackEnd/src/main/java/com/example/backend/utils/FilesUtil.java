package com.example.backend.utils;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;

public class FilesUtil {

    public static String getBase64FromMultipartFile(MultipartFile file) {
        String result = null;
        try {
            result = new String( Base64.encode( file.getBytes() ), "UTF-8" );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void saveBase64IntoFile(MultipartFile file, String fileName) throws IOException {
        String base64 = getBase64FromMultipartFile(file);
        FileWriter fileWriter = new FileWriter(fileName+".txt");
        fileWriter.write(base64);
        fileWriter.flush();
    }
}
