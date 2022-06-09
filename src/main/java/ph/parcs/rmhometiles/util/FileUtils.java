package ph.parcs.rmhometiles.util;

import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

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
        String dirtyPath = Objects.requireNonNull(FileUtils.class.getResource("")).toString();
        String jarPath = dirtyPath.replaceAll("^.*file:/", "");
        jarPath = jarPath.replaceAll("jar!.*", "jar");
        jarPath = jarPath.replaceAll("%20", " ");
        if (!jarPath.endsWith(".jar")) {
            jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
        }
        return Paths.get(jarPath).getParent().toString();
    }

}
