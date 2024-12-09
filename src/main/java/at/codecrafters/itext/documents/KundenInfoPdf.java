package at.codecrafters.itext.documents;

import at.codecrafters.itext.base.PdfBaseDocument;
import at.codecrafters.itext.letter.data.KundenInfoData;
import com.itextpdf.layout.element.Paragraph;

public class KundenInfoPdf extends PdfBaseDocument {

    @Override
    protected void render() throws Exception {
        addLogo();
        KundenInfoData data = (KundenInfoData) obj;
        addDefaultAdressBlock(data.getDocId(), data.getErstellungsDatum(), data.getRecipientAddressLines());

        document.add(new Paragraph("Sehr geehrter Herr " + data.getRecipientName() + ",")
                .setFontSize(12)
                .setBold()
                .setMarginTop(200));

        document.add(new Paragraph("\nwir möchten Sie über unsere neuesten Entwicklungen informieren.")
                .setFontSize(12));

        document.add(new Paragraph("\nMit freundlichen Grüßen,\n\nThomas Muster")
                .setFontSize(12)
                .setItalic());
    }
}
