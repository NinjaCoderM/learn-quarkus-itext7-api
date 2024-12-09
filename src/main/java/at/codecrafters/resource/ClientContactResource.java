package at.codecrafters.resource;

import at.codecrafters.entity.ClientContact;
import at.codecrafters.repository.ClientContactRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/client-contacts")
@Produces("application/json")
@Consumes("application/json")
public class ClientContactResource {

    @Inject
    ClientContactRepository clientContactRepository;

    @GET
    @Transactional
    public List<ClientContact> listAll() {
        return clientContactRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getById(@PathParam("id") Long id) {
        ClientContact clientContact = clientContactRepository.findById(id);
        if (clientContact == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(clientContact).build();
    }

    @POST
    @Transactional
    public Response create(ClientContact clientContact) {
        clientContactRepository.persist(clientContact);
        return Response.status(Response.Status.CREATED).entity(clientContact).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, ClientContact updatedContact) {
        ClientContact existingContact = clientContactRepository.findById(id);
        if (existingContact == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Aktualisieren der Felder
        existingContact.setClient(updatedContact.getClient());
        existingContact.setCategory(updatedContact.getCategory());
        existingContact.setText(updatedContact.getText());
        return Response.ok(existingContact).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = clientContactRepository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}

