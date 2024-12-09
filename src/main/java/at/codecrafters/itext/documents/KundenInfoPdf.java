package at.codecrafters.itext.documents;

import at.codecrafters.itext.base.PdfBaseDocument;
import at.codecrafters.itext.letter.data.KundenInfoData;
import com.itextpdf.layout.element.Paragraph;

@SuppressWarnings("ALL")
public class KundenInfoPdf extends PdfBaseDocument {

    @Override
    protected void render() throws Exception {
        addLogo();
        KundenInfoData data = (KundenInfoData) obj;
        addDefaultAdressBlock(data.docId(), data.erstellungsDatum(), data.getRecipientAddressLines());

        document.add(new Paragraph("Sehr geehrter Herr " + data.recipientName() + ",")
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
