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
    @SuppressWarnings("SameParameterValue")
    record PdfEditRequest(String file2Edit, int pageNumber, int replaceCount, String replacement) {}
    record PdfEditResponse ( String modifiedFileName) {}

    // Konstruktor für Dependency Injection
    public PdfResource(PdfPageEditor editor) {
        this.editor = editor;
    }



    @SuppressWarnings("SameReturnValue")
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
    public Response editPdf(@SuppressWarnings("ClassEscapesDefinedScope") PdfEditRequest request) {
        try {
            java.nio.file.Path baseDir = Paths.get(BASE_DIR).toRealPath();
            java.nio.file.Path file2EditPath = baseDir.resolve(request.file2Edit()).normalize();
            java.nio.file.Path replacementPath = baseDir.resolve(request.replacement()).normalize();
            String fileSuffix = "_modified_"+ LocalDateTime.now().toString().substring(0,19).replace(":", "-");

            if (!file2EditPath.startsWith(baseDir) || !replacementPath.startsWith(baseDir)) {
                logger.error("Ungültiger Pfad.{} {}", file2EditPath, replacementPath);
                return Response.status(Response.Status.FORBIDDEN).entity("Ungültiger Pfad.").build();
            }

            if (!Files.exists(file2EditPath) || !Files.exists(replacementPath)) {
                logger.error("Datei nicht gefunden. {} {}", file2EditPath, replacementPath);
                return Response.status(Response.Status.NOT_FOUND).entity("Datei nicht gefunden. " + file2EditPath + " " + replacementPath).build();
            }

            //PdfPageEditor editor = new PdfPageEditor();

            byte[] replacementPage = PdfUtils.getPdfAsByteArray(baseDir, request.replacement());

            //Act
            byte[] processedPdf = editor.replacePage(new PdfPageEditor.PageSelection4Replacement(request.pageNumber, request.replaceCount()), baseDir, request.file2Edit(), replacementPage);
            String respFileName = request.file2Edit().replace(".pdf", "")+fileSuffix+".pdf";
            PdfUtils.savePdf(processedPdf, baseDir.resolve(respFileName));

            return Response.ok(new PdfEditResponse(respFileName)).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}

