package ph.parcs.rmhometiles.file;


import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static ph.parcs.rmhometiles.util.FileUtils.getTargetPath;

@Service
public class FileService {

    @SneakyThrows
    public void saveImage(String source, String des) {
        Path from = Paths.get(source);
        Path to = Paths.get(des);
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }

    @SneakyThrows
    public void deleteFile(String filename) {
        Path source = Paths.get(getTargetPath(filename));
        Files.deleteIfExists(source);
    }

}
