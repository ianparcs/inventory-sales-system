package ph.parcs.rmhometiles.file;


import javafx.stage.FileChooser;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private static final String BASE_PATH = "image/product" + File.separator;

    public FileChooser getImageChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        return fileChooser;
    }

    @SneakyThrows
    public void saveImage(String source, String des) {
        Path from = Paths.get(source);
        Path to = Paths.get(des);
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }

    public String getFullTargetPath(String filename) {
        String fullPath = getResourcePath() + File.separator + filename;
        return fullPath.trim();
    }

    public String getNewName(String newFileName, String sourcePath) {
        String ext = getFileExtension(sourcePath);
        String fullPath = newFileName + ext;
        return fullPath.trim();
    }

    private String getFileExtension(String sourcePath) {
        File sourceFile = Paths.get(sourcePath).toFile();
        String fileName = sourceFile.getName();
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @SneakyThrows
    private String getResourcePath() {
        URI desPath = ClassLoader.getSystemResource(BASE_PATH).toURI();
        return Paths.get(desPath).toString();
    }

    public URL getResourcePath(String fileName) {
        return getClass().getClassLoader().getResource(BASE_PATH + fileName);
    }
}
