package at.codecrafters.itext.renderer;

import at.codecrafters.itext.documents.KundenInfoPdf;
import at.codecrafters.itext.letter.LetterType;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;

public class PdfRenderer {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static boolean[] bEveryPage = new boolean[] { true, false, false, false, false };
    private final static boolean[] bLastPage = new boolean[] { true, true, false, false, false };
    @SuppressWarnings("unused")
    private final static boolean[] testModus = new boolean[] { true, true, true, true, true };
    private final static boolean[] noPrint = new boolean[] { true, true, false, false, true };

    public <T> byte[] render(LetterType letterType, T data)  {
        if (!letterType.getDataClass().isInstance(data)) {
            throw new IllegalArgumentException("Invalid data type for " + letterType);
        }
        // links, unten, oben, rechts
        final float[] margins = new float[] { 45f, 15f, 70f, 70f };
        byte[] bRet = null;

        try (
            ByteArrayOutputStream bsPass1 = new ByteArrayOutputStream()
        ) {
            PdfWriter writer = new PdfWriter(bsPass1);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document doc = new Document(pdfDocument);
            doc.setMargins(margins[0], margins[1], margins[2], margins[3]);
            PdfCanvas pdfCanvas = new PdfCanvas(pdfDocument.addNewPage());

            boolean duplex = false;
            boolean[] maxStricherl = bLastPage;

            if (letterType==LetterType.KUNDEN_INFO){
                KundenInfoPdf item = new KundenInfoPdf();
                //run(Document document, PdfCanvas pdfCanvas, Object obj)
                item.run(doc, pdfCanvas, data);
                maxStricherl=noPrint;
            }

            logger.info("PDF {} erstellt; Anzahl Seiten: {}", letterType, pdfDocument.getNumberOfPages());

            int n = pdfDocument.getNumberOfPages();

            for (int i = 1; i <= n; i++) {
                PdfPage page = pdfDocument.getPage(i);
                PdfCanvas canvas = new PdfCanvas(page);
                addWatermark(pdfCanvas);

                if (duplex) {
                    if (i % 2 != 0) {
                        if (i < n - 1) {
                            addPrintControlChar(canvas, bEveryPage);
                        } else {
                            addPrintControlChar(canvas, maxStricherl);
                        }
                    }
                } else {
                    if (i < n) {
                        addPrintControlChar(canvas, bEveryPage);
                    } else {
                        addPrintControlChar(canvas, maxStricherl);
                    }
                }

            }
            doc.close();
            bRet = bsPass1.toByteArray();
        } catch (Exception e) {
            logger.error("Error render PDF", e);
        }
        return bRet;
    }

    private void addWatermark(PdfCanvas canvas) throws Exception {
        PdfExtGState gstate = new PdfExtGState();
        gstate.setFillOpacity(0.3f);
        gstate.setStrokeOpacity(0.3f);

        canvas.saveState();
        canvas.setExtGState(gstate);

        PdfFont basicFont = PdfFontFactory.createFont("Helvetica");

        canvas.beginText();
        canvas.setFontAndSize(basicFont, 44);
        canvas.setColor(ColorConstants.MAGENTA, true);
        float x = 122;
        float y = 350;
        float angle = (float) Math.toRadians(45); // Konvertiere Grad in Bogenmaß
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        canvas.concatMatrix(cos, sin, -sin, cos, x, y);
        canvas.showText("Vordruck - nicht versenden");
        canvas.endText();

        canvas.restoreState();
    }

    private void addPrintControlChar(PdfCanvas canvas, boolean[] bStriche) {
        final int x1 = 5;
        final int x2 = 40;
        final int starty = 266;
        final int decy = -12;

        int len = bStriche.length;
        int i = 0;

        canvas.saveState();
        while (i < len) {
            if (bStriche[i]) {
                int tmpy = (starty + (decy * i));
                // Zeichne Linie
                canvas.moveTo(x1, tmpy);
                canvas.lineTo(x2, tmpy);
            }
            i++;
        }
        canvas.stroke();
        canvas.restoreState();
    }


}
