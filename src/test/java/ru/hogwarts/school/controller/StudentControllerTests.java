package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.Collection;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {
    private final long SET_ID = 1;
    private final long SET_ID2 = 22;
    private final long SET_IDD = 2222;
    private final int SET_AGE = 333;
    private final int SET_AGE2 = 222;
    private final String SET_NAME = "Name1";
    private final String SET_NAME2 = "Name22";

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void testGetStudentInfo() throws Exception {
        Student student = new Student(SET_ID, SET_NAME, SET_AGE);
        restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class);
        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/" + SET_ID, Student.class))
                .isEqualTo(student);
    }

    @Test
    void testCreateStudent() {
        Student student = new Student(SET_ID, SET_NAME, SET_AGE);
        Assertions
                .assertThat(restTemplate
                        .postForObject("http://localhost:" + port + "/student", student, Student.class))
                .isNotNull();
    }

    @Test
    void testEditStudent() {
        Student studentOld = restTemplate.getForObject("http://localhost:" + port + "/student/" + SET_ID, Student.class);
        Student studentNew = new Student(SET_ID, SET_NAME2, SET_AGE2);
        restTemplate.put("http://localhost:" + port + "/student", studentNew);
        Assertions
                .assertThat(restTemplate
                        .getForObject("http://localhost:" + port + "/student/" + SET_ID, Student.class))
                .isEqualTo(studentNew);
        restTemplate.put("http://localhost:" + port + "/student", studentOld);
    }

    @Test
    void testAgeFilteredStudents() {
        Student student1 = new Student(SET_ID, SET_NAME, SET_AGE);
        Student student2 = new Student(SET_ID2, SET_NAME2, SET_AGE);
        restTemplate.put("http://localhost:" + port + "/student", student1);
        restTemplate.put("http://localhost:" + port + "/student", student2);
        Assertions.assertThat(restTemplate
                        .getForObject("http://localhost:" + port + "/student/ageFilter/" + SET_AGE, Collection.class).size())
                .isGreaterThan(1);
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student(SET_IDD, SET_NAME, SET_AGE);
        student = restTemplate
                .postForObject("http://localhost:" + port + "/student", student, Student.class);
        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/" + student.getId(), Student.class))
                .isEqualTo(student);
        restTemplate.delete("http://localhost:" + port + "/student" + "/" + student.getId());
        Assertions.assertThat(this.restTemplate
                        .getForObject("http://localhost:" + port + "/student/" + student.getId(), Student.class))
                .isEqualTo(new Student(null, null, 0));
    }
    @Test
    void testFindStudentByAgeBetween() {
        restTemplate.put("http://localhost:" + port + "/student", new Student(SET_ID, SET_NAME, 2005));
        restTemplate.put("http://localhost:" + port + "/student", new Student(SET_ID, SET_NAME, 2006));
        int size = restTemplate
                .getForObject("http://localhost:"
                        + port
                        + "/student/ageBetween/2004&2007", Collection.class)
                .size();
        Assertions.assertThat(size).isGreaterThan(1);
    }
    @Test
    void testgetStudentsFaculty() {
        Assertions.assertThat(restTemplate
                        .getForObject("http://localhost:" + port + "/student/getFacultyOfStudent/"+SET_ID, Faculty.class))
                .isNotNull();
    }
}