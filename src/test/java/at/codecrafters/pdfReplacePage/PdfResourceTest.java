package at.codecrafters.pdfReplacePage;

import at.codecrafters.itext.base.PdfUtils;
import at.codecrafters.itext.editor.PdfPageEditor;
import io.quarkus.test.Mock;
import jakarta.ws.rs.ext.RuntimeDelegate;
import org.jboss.resteasy.reactive.common.jaxrs.RuntimeDelegateImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class PdfResourceTest {

    private static final String BASE_DIR = "src/test/resources/pdf";

    private PdfResource pdfResource;

    @Mock
    private PdfPageEditor mockEditor;

    @Mock
    private PdfUtils mockPdfUtils;

    @BeforeAll
    public static void setUpBeforeAll() {
        RuntimeDelegate.setInstance(new RuntimeDelegateImpl());
    }

    @BeforeEach
    void setUp() {
        // Initialize the mocks
        mockEditor = mock(PdfPageEditor.class);
        mockPdfUtils = mock(PdfUtils.class);
        pdfResource = new PdfResource(mockEditor, mockPdfUtils);
    }

    @Test
    void testEditPdf_Success() throws IOException {
        // Arrange
        try (MockedStatic<PdfUtils> mockedStatic = mockStatic(PdfUtils.class);
             MockedStatic<Files> mockedStatic2 = mockStatic(Files.class)) {
            PdfResource.PdfEditRequest request = new PdfResource.PdfEditRequest("fileToEdit.pdf",  1, 1, "replacement.pdf");
            byte[] mockFile2EditPage = "mockReplacementContent".getBytes();
            byte[] mockReplacementPage = "mockReplacementContent".getBytes();
            byte[] mockProcessedPdf = "mockProcessedContent".getBytes();

            Path baseDir = Paths.get(BASE_DIR).toRealPath();

            mockedStatic2.when(() -> Files.exists(any(Path.class))).thenReturn(true);

            mockedStatic.when(() -> PdfUtils.getPdfAsByteArray(baseDir, request.getReplacement())).thenReturn(mockReplacementPage);
            mockedStatic.when(() -> PdfUtils.getPdfAsByteArray(baseDir, request.getFile2Edit())).thenReturn(mockFile2EditPage);
            when(mockEditor.replacePage(any(), any(), any(), any())).thenReturn(mockProcessedPdf);

            Response response = pdfResource.editPdf(request);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            PdfResource.PdfEditResponse responseBody = (PdfResource.PdfEditResponse) response.getEntity();
            assertEquals("fileToEdit_modified_" + LocalDateTime.now().toString().substring(0, 19).replace(":", "-") + ".pdf",
                    responseBody.getModifiedFileName());

            verify(mockPdfUtils).savePdf(eq(mockProcessedPdf), any());
        }

    }

    @Test
    void testEditPdf_InvalidPath() throws IOException {
        PdfResource.PdfEditRequest request = new PdfResource.PdfEditRequest("../fileToEdit.pdf", 1, 1, "replacement.pdf");

        Response response = pdfResource.editPdf(request);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertEquals("Ungültiger Pfad.", response.getEntity());
    }

    @Test
    void testEditPdf_FileNotFound() throws IOException {
        PdfResource.PdfEditRequest request = new PdfResource.PdfEditRequest("fileToEdit.pdf", 1, 1, "replacement.pdf");

        Response response = pdfResource.editPdf(request);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Datei nicht gefunden"));
    }

    @Test
    public void testGetPdfAsByteArray_throwsIOException() {
        try (MockedStatic<PdfUtils> mockedStatic = mockStatic(PdfUtils.class);
             MockedStatic<Files> mockedStatic2 = mockStatic(Files.class)) {
            PdfResource.PdfEditRequest request = new PdfResource.PdfEditRequest("fileToEdit.pdf",  1, 1, "replacement.pdf");

            mockedStatic2.when(() -> Files.exists(any(Path.class))).thenReturn(true);

            mockedStatic.when(() -> PdfUtils.getPdfAsByteArray(any(Path.class), anyString()))
                    .thenThrow(new IOException("Test error"));

            Response response = pdfResource.editPdf(request);

            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        }
    }
}
