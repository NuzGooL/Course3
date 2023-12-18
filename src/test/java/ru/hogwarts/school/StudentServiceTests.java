package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTests {
    private StudentService service;

    @BeforeEach
    void setService() {
        service = new StudentServiceImpl();
    }

    static Stream<Arguments> arguments() {
        return Stream.of(Arguments.of(1, "Firstname1 Surname1", 11),
                Arguments.of(2, "Firstname2 Surname2", 10),
                Arguments.of(3, "Firstname3 Surname3", 11));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void Create(long id, String name, int age) {
        Student student = new Student(id, name, age);
        service.addStudent(student);
        assertNotNull(student);
        assertEquals(name, student.getName());
        assertEquals(age, student.getAge());
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void Read(long id, String name, int age) {
        Student student = new Student(id, name, age);
        Student createdStudent = service.addStudent(student);
        Student foundedStudent = service.findStudent(createdStudent.getId());
        assertEquals(createdStudent, foundedStudent);
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void Update(long id, String name, int age) {
        Student student = new Student(id, name, age);
        Student createdStudent = service.addStudent(student);
        Student editedStudent = new Student(createdStudent.getId(), "Updated" + name, age);
        Student result = service.editStudent(editedStudent.getId(), editedStudent);
        assertEquals(editedStudent, result);
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void Delete(long id, String name, int age) {
        Student student = new Student(id, name, age);
        Student createdStudent = service.addStudent(student);
        service.deleteStudent(createdStudent.getId());
        assertNull(service.findStudent(createdStudent.getId()));
    }
}