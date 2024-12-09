package at.codecrafters.pdfReplacePage;

import at.codecrafters.itext.base.PdfUtils;
import at.codecrafters.itext.editor.PdfPageEditor;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Path("/pdf")
public class PdfResource {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String BASE_DIR = "C:\\CodeCraftersCompany";
    private final PdfPageEditor editor;
    private final PdfUtils pdfUtils;

    // Konstruktor für Dependency Injection
    public PdfResource(PdfPageEditor editor, PdfUtils pdfUtils) {
        this.editor = editor;
        this.pdfUtils = pdfUtils;
    }



    @GET
    @Path("/greet")
    @Produces(MediaType.TEXT_PLAIN)
    public String greet() {
        logger.info("greet...");
        return "Hello, welcome to our API!";
    }

    @POST
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPdf(PdfEditRequest request) {
        try {
            java.nio.file.Path baseDir = Paths.get(BASE_DIR).toRealPath();
            java.nio.file.Path file2EditPath = baseDir.resolve(request.getFile2Edit()).normalize();
            java.nio.file.Path replacementPath = baseDir.resolve(request.getReplacement()).normalize();
            String fileSuffix = "_modified_"+ LocalDateTime.now().toString().substring(0,19).replace(":", "-");

            if (!file2EditPath.startsWith(baseDir) || !replacementPath.startsWith(baseDir)) {
                logger.error("Ungültiger Pfad." + file2EditPath.toString() + " " + replacementPath.toString() );
                return Response.status(Response.Status.FORBIDDEN).entity("Ungültiger Pfad.").build();
            }

            if (!Files.exists(file2EditPath) || !Files.exists(replacementPath)) {
                logger.error("Datei nicht gefunden. " + file2EditPath.toString() + " " + replacementPath.toString());
                return Response.status(Response.Status.NOT_FOUND).entity("Datei nicht gefunden. " + file2EditPath.toString() + " " + replacementPath.toString()).build();
            }

            //PdfPageEditor editor = new PdfPageEditor();

            byte[] replacementPage = pdfUtils.getPdfAsByteArray(baseDir, request.getReplacement());

            //Act
            byte[] processedPdf = editor.replacePage(new PdfPageEditor.PageSelection4Replacement(request.pageNumber, request.getReplaceCount()), baseDir, request.getFile2Edit(), replacementPage);
            String respFileName = request.getFile2Edit().replace(".pdf", "")+fileSuffix+".pdf";
            pdfUtils.savePdf(processedPdf, baseDir.resolve(respFileName));

            return Response.ok(new PdfEditResponse(respFileName)).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
     static class PdfEditRequest {
        private String file2Edit;
        private int pageNumber;
        private int replaceCount;
        private String replacement;

        // Standard-Konstruktor
        public PdfEditRequest() {}

        // Parameterisierter Konstruktor
        public PdfEditRequest(String file2Edit, int pageNumber, int replaceCount, String replacement) {
            this.file2Edit = file2Edit;
            this.pageNumber = pageNumber;
            this.replaceCount = replaceCount;
            this.replacement = replacement;
        }

        // Getter und Setter
        public String getFile2Edit() {
            return file2Edit;
        }

        public void setFile2Edit(String file2Edit) {
            this.file2Edit = file2Edit;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getReplaceCount() {
            return replaceCount;
        }

        public void setReplaceCount(int replaceCount) {
            this.replaceCount = replaceCount;
        }

        public String getReplacement() {
            return replacement;
        }

        public void setReplacement(String replacement) {
            this.replacement = replacement;
        }
    }
     static class PdfEditResponse {
        private String modifiedFileName;

        // Standard-Konstruktor
        public PdfEditResponse() {}

        // Parameterisierter Konstruktor
        public PdfEditResponse(String modifiedFileName) {
            this.modifiedFileName = modifiedFileName;
        }

        // Getter und Setter
        public String getModifiedFileName() {
            return modifiedFileName;
        }

        public void setModifiedFileName(String modifiedFileName) {
            this.modifiedFileName = modifiedFileName;
        }
    }
}

