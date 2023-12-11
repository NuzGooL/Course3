package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> findStudent(@PathVariable long id) {
        Faculty foundedFaculty = facultyService.findFaculty(id);
        if (foundedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundedFaculty);
    }

    @PostMapping
    public Faculty createStudent(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editStudent(@RequestBody Faculty faculty) {
        Faculty editedFaculty = facultyService.updateFaculty(faculty);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("{id}")
    public Faculty deleteStudent(@PathVariable long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping("/colorFilter/{color}")
    public Collection<Faculty> colorFilteredFaculties(@PathVariable String color) {
        return facultyService.filterColor(color);
    }
}