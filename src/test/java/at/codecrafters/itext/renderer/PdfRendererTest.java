package at.codecrafters.itext.renderer;

import at.codecrafters.itext.base.PdfUtils;
import at.codecrafters.itext.letter.LetterFactory;
import at.codecrafters.itext.letter.LetterType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PdfRendererTest {

    @Test
    void render() {
        try {
            Path testDir = Paths.get("src/test/resources/pdf");

            byte[] pdfAsByte = new PdfRenderer().render(LetterType.KUNDEN_INFO, LetterFactory.createData(LetterType.KUNDEN_INFO));

            Path outputFile = testDir.resolve("output.pdf");
            PdfUtils.savePdf(pdfAsByte, outputFile);
            byte[] savedContent = Files.readAllBytes(testDir.resolve("output.pdf"));

            assertNotNull(pdfAsByte);
            assertArrayEquals(pdfAsByte, savedContent);

            try {
               Files.deleteIfExists(testDir.resolve("output.pdf"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}