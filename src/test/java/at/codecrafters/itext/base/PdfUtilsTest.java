package at.codecrafters.itext.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PdfUtilsTest {
    @TempDir
    Path tempDir;

    @Test
    void testGetPdfAsByteArray() throws IOException {
        String testContent = "Test PDF Content";
        Path testFile = tempDir.resolve("test.pdf");
        Files.writeString(testFile, testContent);

        byte[] result = PdfUtils.getPdfAsByteArray(tempDir, "test.pdf");

        assertNotNull(result);
        assertArrayEquals(testContent.getBytes(), result);
    }

    @Test
    void testGetPdfAsByteArray_FileNotFound() {
        String nonExistentFile = "nonexistent.pdf";

        NoSuchFileException exception = assertThrows(NoSuchFileException.class, () ->
                PdfUtils.getPdfAsByteArray(tempDir, nonExistentFile)
        );
        assertTrue(exception.getMessage().contains(nonExistentFile));
    }

    @Test
    void testSavePdf() throws IOException {
        byte[] pdfContent = "PDF Output Content".getBytes();
        Path outputFile = tempDir.resolve("output.pdf");

        PdfUtils.savePdf(pdfContent, outputFile);

        assertTrue(Files.exists(outputFile));
        byte[] savedContent = Files.readAllBytes(outputFile);
        assertArrayEquals(pdfContent, savedContent);
    }

    @Test
    void testSavePdf_ExceptionHandling() {
        Path invalidPath = tempDir.resolve("invalid/output.pdf");

        IOException exception = assertThrows(IOException.class, () ->
                PdfUtils.savePdf("PDF Content".getBytes(), invalidPath)
        );
        assertNotNull(exception);
    }

}