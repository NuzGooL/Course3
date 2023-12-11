package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final HashMap<Long, Student> students;
    private Long counterID = 0L;

    public StudentService() {
        this.students = new HashMap<>();
    }

    public Student createStudent(Student student) {
        student.setId(++counterID);
        students.put(counterID, student);
        return student;
    }

    public Student findStudent(Long id) {
        return students.get(id);
    }

    public Student updateStudent(Student student) {
        if (!students.containsKey(student.getId())) {
            return null;
        }
        students.put(student.getId(), student);
        return student;
    }

    public Student deleteStudent(Long id) {
        return students.remove(id);
    }

    public Collection<Student> filterAge(int age) {
        return students.values()
                .stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}