package org.lab2.systemarchitecturegroup5assignment3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@ApplicationScoped
public class PetService {

    // ConcurrentHashMap allows multiple threads to safely access the pets.
    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();

    // AtomicLong creates a different ID for each new pet.
    private final AtomicLong nextId = new AtomicLong(1);

    // The lock makes feeding and playing one complete, safe operation.
    private final ReentrantLock lock = new ReentrantLock();

    // Adds a new pet and returns its generated ID.
    public long createPet(PetDTO pet) {
        long id = nextId.getAndIncrement();
        pets.put(id, pet);
        return id;
    }

    // Returns all pets currently stored in the service.
    public List<PetDTO> getAllPets(String sortBy, String order) {
        List<PetDTO> petList = new ArrayList<>(pets.values());

        if (sortBy == null || sortBy.isBlank()) {
            return petList; // Return unsorted if parameter is missing
        }

        // Determine property comparison strategy
        Comparator<PetDTO> comparator = switch (sortBy.toLowerCase()) {
            case "name" -> Comparator.comparing(PetDTO::name, String.CASE_INSENSITIVE_ORDER);
            case "species" -> Comparator.comparing(PetDTO::species, String.CASE_INSENSITIVE_ORDER);
            case "hungerlevel" -> Comparator.comparing(PetDTO::hungerLevel);
            case "happiness" -> Comparator.comparing(PetDTO::happiness);
            default -> null;
        };

        if (comparator != null) {
            // Apply reverse ordering if 'desc' is requested
            if ("desc".equalsIgnoreCase(order)) {
                comparator = comparator.reversed();
            }
            petList.sort(comparator);
        }

        return petList;
    }

    // Finds one pet by ID or reports that the pet does not exist.
    public PetDTO getPetById(long id) {
        PetDTO pet = pets.get(id);
        if (pet == null) {
            throw new NotFoundException("Pet with ID " + id + " not found");
        }
        return pet;
    }

    // Feeding lowers the hunger level by 10, but never below zero.
    public void feedPet(long id) {
        lock.lock();
        try {
            PetDTO pet = getPetById(id);
            pets.put(id, new PetDTO(
                    pet.name(),
                    pet.species(),
                    Math.max(0, pet.hungerLevel() - 10),
                    pet.happiness()
            ));
        } finally {
            lock.unlock();
        }
    }

    // Playing increases happiness by 10, but never above 100.
    public void playWithPet(long id) {
        lock.lock();
        try {
            PetDTO pet = getPetById(id);
            pets.put(id, new PetDTO(
                    pet.name(),
                    pet.species(),
                    pet.hungerLevel(),
                    Math.min(100, pet.happiness() + 10)
            ));
        } finally {
            lock.unlock();
        }
    }

    // Removes a pet from the service.
    public void deletePet(long id) {
        if (pets.remove(id) == null) {
            throw new NotFoundException("Pet with ID " + id + " not found");
        }
    }
}
