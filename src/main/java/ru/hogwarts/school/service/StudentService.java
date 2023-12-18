package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

public interface StudentService {
    Student addStudent(Student faculty);

    Student findStudent(Long facultyId);

    Student editStudent(Long facultyId, Student faculty);

    void deleteStudent(Long facultyId);
}