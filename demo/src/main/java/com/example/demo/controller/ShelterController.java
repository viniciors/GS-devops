package com.example.demo.controller;

import com.example.demo.model.Shelter;
import com.example.demo.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelters")
public class ShelterController {

    @Autowired
    private ShelterRepository repo;

    @GetMapping
    public List<Shelter> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shelter> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Shelter create(@RequestBody Shelter shelter) {
        return repo.save(shelter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shelter> update(@PathVariable Long id, @RequestBody Shelter s) {
        return repo.findById(id).map(existing -> {
            existing.setName(s.getName());
            existing.setLatitude(s.getLatitude());
            existing.setLongitude(s.getLongitude());
            existing.setCapacity(s.getCapacity());
            repo.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
