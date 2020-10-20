package ph.parcs.rmhometiles.util;

import javafx.stage.FileChooser;
import lombok.SneakyThrows;
import ph.parcs.rmhometiles.file.FileImage;

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
        String fullPath = getResourcePath() + File.separator + filename;
        return fullPath.trim();
    }

    @SneakyThrows
    private static String getResourcePath() {
        URI desPath = ClassLoader.getSystemResource(BASE_PATH).toURI();
        return Paths.get(desPath).toString();
    }

    public static URL getResourcePath(String fileName) {
        return FileUtils.class.getClassLoader().getResource(FileUtils.BASE_PATH + fileName);
    }
}
