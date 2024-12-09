package at.codecrafters.resource;

import at.codecrafters.entity.Client;
import at.codecrafters.repository.ClientRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {
    private final PanacheRepositoryBase<Client, Long> clientRepository;

    public ClientResource(PanacheRepositoryBase<Client, Long> clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GET
    @Transactional
    public List<Client> getClients() {
        return clientRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getClientById(@PathParam("id") Long id) {
        Client client = clientRepository.findById(id);
        if (client == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(client).build();
    }

    @POST
    @Transactional
    public Response createClient(Client client) {
        clientRepository.persist(client);
        return Response.status(Response.Status.CREATED).entity(client).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateClient(@PathParam("id") Long id, Client client) {
        Client existingClient = clientRepository.findById(id);
        if (existingClient == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existingClient.setFirstname(client.getFirstname());
        existingClient.setLastname(client.getLastname());
        existingClient.setCompany(client.getCompany());
        return Response.ok(existingClient).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteClient(@PathParam("id") Long id) {
        Client existingClient = clientRepository.findById(id);
        if (existingClient == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        clientRepository.delete(existingClient);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
