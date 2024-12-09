package at.codecrafters.itext.editor;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class PdfPageEditor {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public record PageSelection4Replacement(int pageNumber, int count) {}

    public byte[] replacePage(PageSelection4Replacement replaceSelection, Path path, String origFilename, byte[] replacementPage){
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PdfDocument modPdfDoc = new PdfDocument(new PdfReader(Files.newInputStream(path.resolve(origFilename))), new PdfWriter(outputStream));
             PdfDocument replacePageDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(replacementPage)))){

            logger.info("Start ... replace page number "
                    + IntStream.range(replaceSelection.pageNumber(), replaceSelection.pageNumber() + replaceSelection.count()).mapToObj(String::valueOf).collect(Collectors.joining(", "))
                    + " from original " + origFilename + ", add " + replacePageDoc.getNumberOfPages() + (replacePageDoc.getNumberOfPages()==1?" page":" pages"));

            IntStream.range(replaceSelection.pageNumber(), replaceSelection.pageNumber() + replaceSelection.count())
                    .map(i -> replaceSelection.pageNumber() - (i - (replaceSelection.pageNumber() + replaceSelection.count() - 1))).forEach(modPdfDoc::removePage);

            for (int i=1; i<=replacePageDoc.getNumberOfPages(); i++) {
                PdfPage importedPage = replacePageDoc.getPage(i);
                PdfPage pageEmpty = modPdfDoc.addNewPage(replaceSelection.pageNumber()+i-1);
                PdfCanvas canvas = new PdfCanvas(pageEmpty);
                canvas.addXObjectAt(importedPage.copyAsFormXObject(modPdfDoc), 0, 0);
            }

            modPdfDoc.close();

            logger.info("Finished replacing page(s).");
            return outputStream.toByteArray();
        } catch (IOException e) {
            logger.error("Error replacing page", e);
            throw new RuntimeException(e);
        }
    }
}
