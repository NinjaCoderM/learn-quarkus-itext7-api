package at.codecrafters.itext.base;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@ApplicationScoped
public class PdfUtils {
    public static byte[] getPdfAsByteArray(Path testDir, String origFilename) throws IOException {
            return Files.readAllBytes(testDir.resolve(origFilename));
    }
    public static void savePdf(byte[] processedPdf, Path outputPath) throws IOException {
            Files.write(outputPath, processedPdf, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("PDF erfolgreich gespeichert: " + outputPath);
    }
}
