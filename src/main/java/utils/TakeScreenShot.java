package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakeScreenShot {

    public static void takeScreenShot(TakesScreenshot screenShot){
        String fileName = createFileName();
        File scrFile = screenShot.getScreenshotAs(OutputType.FILE);
        try {
            Files.createDirectories(Path.of("build/screenshots"));
            Files.copy(scrFile.toPath(), Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String createFileName(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss-SSS");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(date);
        return "build/screenshots/scr-" + currentDate + ".png";
    }
}
