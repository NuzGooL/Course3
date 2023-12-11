package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {
    private StudentService service;

    @BeforeEach
    void setService() {
        service = new StudentService();
    }

    static Stream<Arguments> arguments() {
        return Stream.of(Arguments.of(1, "Harry Potter", 11),
                Arguments.of(2, "Hermione Granger", 10),
                Arguments.of(3, "Ronald Weasley", 11, 3));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void willReturnCreatedStudent(long id, String name, int age) {
        Student student = new Student(id, name, age);
        service.createStudent(student);
        assertNotNull(student);
        assertEquals(name, student.getName());
        assertEquals(age, student.getAge());
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void willReturnFoundedStudent(long id, String name, int age) {
        Student student = new Student(id, name, age);
        Student createdStudent = service.createStudent(student);
        Student foundedStudent = service.findStudent(createdStudent.getId());
        assertEquals(createdStudent, foundedStudent);
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void willReturnEditedStudent(long id, String name, int age) {
        Student student = new Student(id, name, age);
        Student createdStudent = service.createStudent(student);
        Student editedStudent = new Student(createdStudent.getId(), "Updated" + name, age);
        Student result = service.updateStudent(editedStudent);
        assertEquals(editedStudent, result);
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void willReturnDeletedStudent(long id, String name, int age) {
        Student student = new Student(id, name, age);
        Student createdStudent = service.createStudent(student);
        Student deletedStudent = service.deleteStudent(createdStudent.getId());
        assertNotNull(deletedStudent);
        assertEquals(createdStudent, deletedStudent);
        Student notFondedStudent = service.findStudent(deletedStudent.getId());
        assertNull(notFondedStudent);
    }

    @Test
    void filterAge() {
        StudentService studentService = new StudentService();
        Student student1 = new Student(1L, "David", 25);
        Student student2 = new Student(2L, "Eva", 25);
        Student student3 = new Student(3L, "Frank", 30);

        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);

        Collection<Student> filteredStudents = studentService.filterAge(25);

        assertEquals(2, filteredStudents.size());
        assertEquals(25, student1.getAge());
        assertEquals(25, student2.getAge());
    }
}