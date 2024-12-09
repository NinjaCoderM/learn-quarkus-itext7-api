package at.codecrafters.resource;

import at.codecrafters.entity.Category;
import at.codecrafters.repository.CategoryRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    CategoryRepository categoryRepository;

    @GET
    public List<Category> getAllCategories() {
        return categoryRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getCategoryById(@PathParam("id") Long id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(category).build();
    }

    @POST
    @Transactional
    public Response createCategory(Category category) {
        categoryRepository.persist(category);
        return Response.status(Response.Status.CREATED).entity(category).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateCategory(@PathParam("id") Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id);
        if (existingCategory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existingCategory.setName(category.getName());
        return Response.ok(existingCategory).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteCategory(@PathParam("id") Long id) {
        Category existingCategory = categoryRepository.findById(id);
        if (existingCategory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        categoryRepository.delete(existingCategory);
        return Response.noContent().build();
    }
}
