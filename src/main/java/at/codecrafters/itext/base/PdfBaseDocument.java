package at.codecrafters.itext.base;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.apache.commons.lang3.StringUtils;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class PdfBaseDocument {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String[] SENDER = new String[] { "CodeFactory", "Triester Straße 40 / 6 / 504, 1100 Wien", "Österreich", " ", "Telefon +43(0)... DW {0}", "Fax +43(0)... DW {1}", "", "{2}" };

    private static Image imgLogo;

    protected PdfCanvas pdfCanvas = null;
    protected Document document = null;
    protected Object obj = null;
    protected FontProgram fontProgram = null;

    private String telNr="1222";
    private String faxNr="14555";
    private String email="wien.m.scheibelreiter@gmail.com";

    static {
        try {
            ImageData imageData = ImageDataFactory.create(Objects.requireNonNull(PdfBaseDocument.class.getClassLoader().getResource("logo.jpg")));
            imgLogo = new Image(imageData);
            imgLogo.setWidth(250);
        } catch (Exception ex) {
            imgLogo = null;
            logger.error("Error setting Image" , ex);
        }
    }

    protected abstract void render() throws Exception;

    public void run(Document document, PdfCanvas pdfCanvas, Object obj) throws Exception {
        this.document = document;
        this.pdfCanvas = pdfCanvas;
        this.obj = obj;
        fontProgram = FontProgramFactory.createFont("ARIALUNI.TTF");
        render();
    }

    @SuppressWarnings("SameParameterValue")
    protected void addLogo(float x, float y)  {
        if (imgLogo != null) {
            imgLogo.setFixedPosition(x, y);
            document.add(imgLogo);
        }
    }

    protected void addLogo()  {
        addLogo(296f, 765f);
    }

    protected void addDefaultAdressBlock(String docid, LocalDate erstellungDat, String... recipient) throws IOException {

        boolean diak_enable = Arrays.stream(recipient).anyMatch(s-> s!=null && !StandardCharsets.ISO_8859_1.newEncoder().canEncode(s));

        PdfFont arialuniFont = diak_enable?PdfFontFactory.createFont(fontProgram, "Identity-H",  PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED):null;
        PdfFont bf1 = PdfFontFactory.createFont("Helvetica");

        Rectangle rectangle = new Rectangle(608.0f - this.document.getRightMargin() - 275, 620, 250f, 30f);
        addTextLign("DocID: ", docid, TextAlignment.RIGHT, rectangle, arialuniFont==null?bf1:arialuniFont, 11f);

        logger.info("DocID: {}", docid);

        rectangle = new Rectangle(608.0f - this.document.getRightMargin() - 125, 600, 100f, 30f);
        addTextLign("", "Wien " + erstellungDat.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) , TextAlignment.RIGHT, rectangle, arialuniFont==null?bf1:arialuniFont, 11f);

        int i = 0;
        for (String s : SENDER) {
            if (s.contains("{0}")){
                s = s.replace("{0}", telNr);
            }
            if (s.contains("{1}")){
                s = s.replace("{1}", faxNr);
            }
            if (s.contains("{2}")){
                s = s.replace("{2}", email);
            }
            rectangle = new Rectangle(345, 730 - i++ * 10, 200f, 30f);
            addTextLign("", s , TextAlignment.RIGHT, rectangle, arialuniFont==null?bf1:arialuniFont, 9f);
        }

        addAdressBlock(this.document.getLeftMargin()+30, 690, recipient);
    }

    private void addTextLign(String prefixText, String mainText, TextAlignment textAlignment, Rectangle rectangle, PdfFont font, float fontSize) {
        if(StringUtils.isNotEmpty(mainText)) {
            Canvas canvas = new Canvas(pdfCanvas, rectangle);
            Paragraph p = new Paragraph().add(prefixText +" " + mainText)
                    .setTextAlignment(textAlignment)
                    .setFont(font)
                    .setFontSize(fontSize);
            canvas.add(p);
            canvas.close();
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void addAdressBlock(float x, float startY, String... adr) {
        PdfFont arialuniFont = PdfFontFactory.createFont(fontProgram, "Identity-H",  PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        int i = 0;
        for (String s : adr) {
            if (s != null){
                Rectangle rectangle = new Rectangle( x, startY - i++ * 12, 150f, 30f);
                addTextLign("", s, TextAlignment.LEFT, rectangle, arialuniFont, 11f);
            }
        }
    }

    public String getTelNr() {
        return telNr;
    }

    public void setTelNr(String telNr) {
        this.telNr = telNr;
    }

    public String getFaxNr() {
        return faxNr;
    }

    public void setFaxNr(String faxNr) {
        this.faxNr = faxNr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
