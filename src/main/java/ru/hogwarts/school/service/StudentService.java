package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student addStudent(Student faculty);

    Student findStudent(Long facultyId);

    Student editStudent(Student faculty);

    void deleteStudent(Long facultyId);

    Collection<Student> filterAge(int age);
}