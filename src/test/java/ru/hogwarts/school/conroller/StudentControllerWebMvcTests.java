package ru.hogwarts.school.conroller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerWebMvcTests {
    private final long ID = 1;
    private final long ID2 = 2;
    private final String NAME = "Name";
    private final String NAME2 = "Name2";
    private final int AGE = 10;
    private final int AGE2 = 20;
    private final int MIN = 9;
    private final int MAX = 21;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private StudentServiceImpl studentService;
    @InjectMocks
    private StudentController studentController;

    @Test
    public void testGetStudentInfo() throws Exception {

        Student student = new Student(ID, NAME, AGE);
        when(studentRepository.findById(ID)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE));
    }

    @Test
    void createStudentTest() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", NAME);
        jsonObject.put("age", AGE);

        Student student = new Student();
        student.setAge(AGE);
        student.setName(NAME);
        student.setId(ID);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.age").value(AGE));

        verify(studentRepository, times(1)).save(argThat(savedStudent ->
                savedStudent.getName().equals(NAME) && savedStudent.getAge() == AGE
        ));
    }

    @Test
    public void testEditStudent() throws Exception {
        JSONObject studentObject = new JSONObject();
        studentObject.put("id", ID);
        studentObject.put("name", NAME);
        studentObject.put("age", AGE2);

        Student curStudent = new Student(ID, NAME, AGE);
        Student newStudent = new Student(ID, NAME, AGE2);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(curStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE2));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Student student = new Student(ID, NAME, AGE);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindStudentByAgeBetween() throws Exception {
        List<Student> students = List.of(
                new Student(ID, NAME, AGE),
                new Student(ID2, NAME2, AGE2)
        );

        when(studentRepository.findByAgeBetween(anyInt(), anyInt())).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/ageBetween/"
                                + MIN + "&" + MAX))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "[" +
                                "{id:" + ID + ",name:" + NAME + ",age:" + AGE + ",faculty:null}," +
                                "{id:" + ID2 + ",name:" + NAME2 + ",age:" + AGE2 + ",faculty:null}" +
                                "]"));
    }
}