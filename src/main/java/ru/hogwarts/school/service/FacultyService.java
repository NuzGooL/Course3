package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

public interface FacultyService {
    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long facultyId);

    Faculty editFaculty(Long facultyId, Faculty faculty);

    void deleteFaculty(Long facultyId);
}