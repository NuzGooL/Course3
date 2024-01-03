package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTests {
    private final long SET_ID = 4;
    private final long SET_ID2 = 22;
    private final long SET_IDD = 400;
    private final String SET_COLOR = "Color1";
    private final String SET_COLOR2 = "Color2";
    private final String SET_COLOR3 = "Color3";
    private final String SET_COLOR_DELETED = "ColorDeleted7";
    private final String SET_NAME = "FName1";
    private final String SET_NAME2 = "FName22";
    private final String SET_NAME3 = "FName3";
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void createFaculty() {
        Faculty faculty = new Faculty(SET_ID, SET_NAME, SET_COLOR);
        faculty = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Assertions.assertThat(restTemplate
                .getForObject("http://localhost:" + port + "/faculty/" + faculty.getId(), Faculty.class)).isEqualTo(faculty);
    }

    @Test
    void editFaculty() {
        Faculty newFaculty = new Faculty(SET_ID, SET_NAME2, SET_COLOR2);
        restTemplate.put("http://localhost:" + port + "/faculty", newFaculty);

        Assertions.assertThat(
                        restTemplate.getForObject("http://localhost:" + port + "/faculty/" + SET_ID, Faculty.class))
                .isEqualTo(newFaculty);
    }

    @Test
    void getFacultyInfo() {
        restTemplate.postForObject("http://localhost:" + port + "/faculty", new Faculty(SET_ID, SET_NAME, SET_COLOR), Faculty.class);
        Faculty faculty = restTemplate
                .getForObject("http://localhost:" + port + "/faculty/" + SET_ID, Faculty.class);
        Assertions.assertThat(faculty).isNotNull();
    }

    @Test
    void deleteFaculty() {
        Faculty faculty = new Faculty(SET_ID2, SET_NAME2, SET_COLOR2);
        restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        long id = faculty.getId();
        restTemplate.delete("http://localhost:" + port + "/faculty/" + faculty.getId());
        Assertions.assertThat(restTemplate
                        .getForObject("http://localhost:" + port + "/faculty/" + id, Faculty.class))
                .isEqualTo(new Faculty(null, null, null));
    }

    @Test
    void findFaculties() {
        Faculty faculty = new Faculty(SET_IDD, SET_NAME, SET_COLOR_DELETED);
        Faculty faculty2 = new Faculty(SET_IDD+1, SET_NAME2, SET_COLOR_DELETED);
        faculty = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        faculty2 = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty2, Faculty.class);

        Assertions.assertThat(restTemplate
                        .getForObject("http://localhost:" + port + "/faculty/colorFilter/" + SET_COLOR_DELETED, Collection.class).size())
                .isEqualTo(2);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + faculty.getId());
        restTemplate.delete("http://localhost:" + port + "/faculty/" + faculty2.getId());
    }

    @Test
    void findFacultyByColorOrName() {
        restTemplate.put("http://localhost:" + port + "/faculty", new Faculty(1l, SET_NAME3, SET_COLOR3));

        Assertions.assertThat(restTemplate
                        .getForObject("http://localhost:" + port + "/faculty/findByColorOrName?name=" + SET_NAME3, Faculty.class))
                .isEqualTo(new Faculty(1L, SET_NAME3, SET_COLOR3));
        Assertions.assertThat(restTemplate
                        .getForObject("http://localhost:" + port + "/faculty/findByColorOrName?color=" + SET_COLOR3, Faculty.class))
                .isEqualTo(new Faculty(1L, SET_NAME3, SET_COLOR3));
    }

    @Test
    void getFacultyOfStudent() {
        Faculty faculty = new Faculty(1l, SET_NAME3, SET_COLOR3);
        Student student = new Student(SET_ID, SET_NAME3, 0);
        student.setFaculty(faculty);
        restTemplate.put("http://localhost:" + port + "/faculty", faculty);
        Assertions.assertThat(
                        restTemplate.getForObject("http://localhost:" + port + "/faculty/studentsOfFaculty/1", Collection.class))
                .isNotNull();
    }
}