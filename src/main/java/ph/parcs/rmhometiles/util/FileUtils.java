package ph.parcs.rmhometiles.util;

import javafx.stage.FileChooser;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;

public class FileUtils {

    private static final String BASE_PATH = "image/product" + File.separator;

    public static FileChooser getImageChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        return fileChooser;
    }

    public static String getFileName(String path) {
        return new File(path).getName();
    }

    public static String getTargetPath(String filename) {
        String fullPath = getParentDirectoryFromJar() + File.separator + filename;
        return fullPath.trim();
    }
    public static String getParentDirectoryFromJar() {
        String dirtyPath = FileUtils.class.getResource("").toString();
        String jarPath = dirtyPath.replaceAll("^.*file:/", ""); //removes file:/ and everything before it
        jarPath = jarPath.replaceAll("jar!.*", "jar"); //removes everything after .jar, if .jar exists in dirtyPath
        jarPath = jarPath.replaceAll("%20", " "); //necessary if path has spaces within
        if (!jarPath.endsWith(".jar")) { // this is needed if you plan to run the app using Spring Tools Suit play button.
            jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
        }
        return Paths.get(jarPath).getParent().toString();
    }

    public static URL getResourcePath(String fileName) {
        return FileUtils.class.getClassLoader().getResource(FileUtils.BASE_PATH + fileName);
    }
}
