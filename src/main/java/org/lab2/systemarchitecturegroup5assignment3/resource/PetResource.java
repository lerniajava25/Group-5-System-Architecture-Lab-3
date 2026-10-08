package org.lab2.systemarchitecturegroup5assignment3.resource;

import org.lab2.systemarchitecturegroup5assignment3.PetDTO;
import org.lab2.systemarchitecturegroup5assignment3.PetService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.Pattern;

/**
 * JAX-RS Resource class exposing RESTful web service endpoints for pet management.
 * Returns manually ordered JSON payloads to avoid automatic alphabetical record sorting.
 */
@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetResource {

    @Inject
    private PetService petService;

    // Helper method to enforce the exact property order using a LinkedHashMap
    private Map<String, Object> convertToSortedMap(PetDTO pet) {
        Map<String, Object> sortedMap = new LinkedHashMap<>();
        sortedMap.put("name", pet.name());
        sortedMap.put("species", pet.species());
        sortedMap.put("hungerLevel", pet.hungerLevel());
        sortedMap.put("happiness", pet.happiness());
        return sortedMap;
    }

    /**
     * POST /pets -> Adopt a new pet.
     */
    @POST
    public Response adoptPet(@Valid PetDTO petDTO) {
        long generatedId = petService.createPet(petDTO);
        PetDTO createdPet = petService.getPetById(generatedId);

        // Returns HTTP 201 with perfectly ordered fields
        return Response.status(Response.Status.CREATED)
                .entity(convertToSortedMap(createdPet))
                .build();
    }

    /**
     * GET /pets -> List all adopted pets currently stored in memory.
     */
    @GET
    public Response listAllPets(
            @QueryParam("sortBy")
            @Pattern(regexp = "^(?i)(name|species|hungerLevel|happiness)$",
                    message = "Invalid sort field. Allowed values are: name, species, hungerLevel, happiness")
            String sortBy,
            @QueryParam("order")
            @Pattern(regexp = "^(?i)(asc|desc)$",
                    message = "Invalid sort order. Allowed values are: asc, desc")
            String order)  {
        List<PetDTO> allPets = petService.getAllPets(sortBy, order);
        List<Map<String, Object>> sortedPetsList = new ArrayList<>();

        for (PetDTO pet : allPets) {
            sortedPetsList.add(convertToSortedMap(pet));
        }

        return Response.ok(sortedPetsList).build();
    }

    /**
     * GET /pets/{id} -> View specific pet status by path id parameters.
     */
    @GET
    @Path("/{id}")
    public Response viewPetStatus(@PathParam("id") long id) {
        PetDTO pet = petService.getPetById(id);
        return Response.ok(convertToSortedMap(pet)).build();
    }

    /**
     * PUT /pets/{id}/feed -> Feed the pet to decrease hunger levels.
     */
    @PUT
    @Path("/{id}/feed")
    public Response feedPet(@PathParam("id") long id) {
        petService.feedPet(id);
        PetDTO updatedPet = petService.getPetById(id);

        // Returns HTTP 200 with perfectly ordered fields
        return Response.ok(convertToSortedMap(updatedPet)).build();
    }

    /**
     * PUT /pets/{id}/play -> Play with the pet to increase happiness levels.
     */
    @PUT
    @Path("/{id}/play")
    public Response playWithPet(@PathParam("id") long id) {
        petService.playWithPet(id);
        PetDTO updatedPet = petService.getPetById(id);

        // Returns HTTP 200 with perfectly ordered fields
        return Response.ok(convertToSortedMap(updatedPet)).build();
    }

    /**
     * DELETE /pets/{id} -> Release/delete the pet from memory.
     */
    @DELETE
    @Path("/{id}")
    public Response releasePet(@PathParam("id") long id) {
        petService.deletePet(id);
        return Response.noContent().build();
    }
}

