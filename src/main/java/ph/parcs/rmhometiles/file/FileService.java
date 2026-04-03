package ph.parcs.rmhometiles.file;


import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.file.writer.ExcelWriter;
import ph.parcs.rmhometiles.util.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static ph.parcs.rmhometiles.util.FileUtils.getTargetPath;

@Service
public class FileService {

    @SneakyThrows
    public void saveImage(ImageProduct imageProduct) {
        String fileName = FileUtils.getFileName(imageProduct.getPath());
        String des = FileUtils.getTargetPath(fileName);
        String src = imageProduct.getPath();
        imageProduct.setName(fileName);
        imageProduct.setPath(des);

        Path from = Paths.get(src);
        Path to = Paths.get(des);
        Files.createDirectories(to.getParent());
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }

    @SneakyThrows
    public void deleteFile(String filename) {
        Path source = Paths.get(getTargetPath(filename));
        Files.deleteIfExists(source);
    }

    @SneakyThrows
    public void exportToExcel(ExcelWriter excelWriter) {
        excelWriter.write();
    }

}
