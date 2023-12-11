package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private Long counterID = 0L;
    private final HashMap<Long, Faculty> facultyMap;

    public FacultyService() {
        this.facultyMap = new HashMap<>();
    }

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++counterID);
        facultyMap.put(counterID, faculty);
        return faculty;
    }

    public Faculty findFaculty(Long id) {
        return facultyMap.get(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (!facultyMap.containsKey(faculty.getId())) {
            return null;
        }
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteFaculty(Long id) {
        return facultyMap.remove(id);
    }

    public Collection<Faculty> filterColor(String color) {
        return facultyMap.values()
                .stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}