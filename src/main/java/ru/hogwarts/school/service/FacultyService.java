package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface FacultyService {
    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long facultyId);

    Faculty editFaculty(Faculty faculty);

    void deleteFaculty(Long facultyId);

    Collection<Faculty> filterColor(String color);

    Faculty findByName(String name);

    Faculty findByColor(String color);

    Collection<Student> getStudentsOfFaculty(long facultyId);
}