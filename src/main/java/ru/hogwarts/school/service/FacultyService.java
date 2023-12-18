package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long facultyId);

    Faculty editFaculty(Faculty faculty);

    void deleteFaculty(Long facultyId);

    Collection<Faculty> filterColor(String color);
}