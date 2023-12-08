package org.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class FileUtil {

    public static String formatDateForFile(LocalDateTime time){
        DateTimeFormatter dateFormatter
                = DateTimeFormatter.ofPattern("yyMMddHHmmss");


        return time.format(dateFormatter);
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[500];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }
}
