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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerWebMvcTests {
    private final long ID = 1;
    private final String NAME = "Name";
    private final String COLOR = "NoName1";
    private final String COLOR2 = "NoName2";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyServiceImpl facultyService;
    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void getFacultyInfo() throws Exception {
        Faculty faculty = new Faculty(ID, NAME, COLOR);
        when(facultyRepository.findById(ID)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));
    }

    @Test
    public void createFaculty() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", NAME);
        facultyObject.put("color", COLOR);
        Faculty faculty = new Faculty(ID, NAME, COLOR);

        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));
    }

    @Test
    public void editFaculty() throws Exception {
        JSONObject studentObject = new JSONObject();
        studentObject.put("id", ID);
        studentObject.put("name", NAME);
        studentObject.put("color", COLOR2);

        Faculty curFaculty = new Faculty(ID, NAME, COLOR);
        Faculty newFaculty = new Faculty(ID, NAME, COLOR2);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(curFaculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(newFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR2));
    }

    @Test
    public void deleteFaculty() throws Exception {
        Faculty faculty = new Faculty(ID, NAME, COLOR);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findFacultyByColorOrName() throws Exception {
        long id = 1;
        String name = "Математика";
        String color = "Синий";
        Faculty faculty = new Faculty(ID, NAME, COLOR);

        when(facultyRepository.findFirstFacultyByNameIgnoreCase(any())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/findByColorOrName?name=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));

        when(facultyRepository.findFirstFacultyByColorIgnoreCase(any())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/findByColorOrName?color=" + COLOR))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));
    }
}