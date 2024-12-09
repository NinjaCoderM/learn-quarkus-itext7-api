package at.codecrafters.itext.editor;

import at.codecrafters.itext.base.PdfUtils;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PdfPageEditorTest {

    @Test
    void replacePage() throws IOException {
        // Arrange
        Path testDir = Paths.get("src/test/resources/pdf");
        String origFilename = "original.pdf";
        String replacePageFilename = "replacement.pdf";
        String fileSuffix = "_modified_"+ LocalDateTime.now().toString().substring(0,19).replace(":", "-");
        PdfPageEditor editor = new PdfPageEditor();

        byte[] replacementPage = PdfUtils.getPdfAsByteArray(testDir, replacePageFilename);

        //Act
        byte[] processedPdf = editor.replacePage(new PdfPageEditor.PageSelection4Replacement(2,2), testDir, origFilename, replacementPage);
        PdfUtils.savePdf(processedPdf, testDir.resolve(origFilename.replace(".pdf", "")+fileSuffix+".pdf"));

        // Assert
        String modFileName = origFilename.replace(".pdf", "") + fileSuffix + ".pdf";
        Path modifiedFilePath = testDir.resolve(modFileName);

        // Prüfen, ob die Datei erstellt wurde
        assertTrue(Files.exists(modifiedFilePath));

        // Prüfen, ob die Anzahl der Seiten korrekt ist
        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(Files.newInputStream(modifiedFilePath)))) {
            assertEquals( 5, pdfDoc.getNumberOfPages());
        }  catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Files.deleteIfExists(modifiedFilePath);
            } catch (IOException e) {
                //noinspection ThrowFromFinallyBlock
                throw new RuntimeException(e);
            }
        }

    }




}