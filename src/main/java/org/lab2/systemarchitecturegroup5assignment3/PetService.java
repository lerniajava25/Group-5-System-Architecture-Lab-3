package org.lab2.systemarchitecturegroup5assignment3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

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
    public List<PetDTO> getAllPets() {
        return List.copyOf(pets.values());
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
