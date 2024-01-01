package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchoolApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private StudentServiceImpl studentServiceImpl;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() throws Exception {
		Assertions.assertThat(studentServiceImpl).isNotNull();
	}
	@Autowired
	private StudentRepository studentRepository;

	@Test
	public void testGetStudent() throws Exception {
		Assertions
				.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/1", String.class))
				.isNotNull();
	}

	@Test
	public void testPostUser() throws Exception {
		Student student = new Student();
		student.setId(1L);
		student.setName("Name1");
		student.setAge(223);

		Assertions
				.assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
				.isNotNull();
	}

	@Test
	void testCreateStudent() throws Exception {
		Student expect = new Student();
		expect.setName("Name1");
		expect.setAge(56);
		studentRepository.save(expect);
		Student student = this.restTemplate.postForObject(
				"http://localhost:" + port + "/student", expect, Student.class);
		Assertions.assertThat(student).isNotNull();

		ResponseEntity<Student> responseEntity = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/student/" + student.getId(), Student.class);

		Assertions.assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
		Assertions.assertThat(expect).isEqualTo(responseEntity.getBody());
	}

	@Test
	void testFindStudent() throws Exception {
		Student expect = new Student();
		expect.setName("Name1");
		expect.setAge(56);
		studentRepository.save(expect);
		Student student = this.restTemplate.getForObject(
				"http://localhost:" + port + "/student/" + expect.getId(), Student.class);

		Assertions.assertThat(student).isNotNull();
		Assertions.assertThat(student).isEqualTo(expect);

		ResponseEntity<Student> responseEntity = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/student/" + student.getId(), Student.class);
		Assertions.assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
		Assertions.assertThat(expect).isEqualTo(responseEntity.getBody());
	}

	@Test
	void testEditStudent() throws Exception {
		Student expect = new Student();
		expect.setName("Name1");
		expect.setAge(56);
		studentRepository.save(expect);
		Student changedStudent = new Student(expect.getId(), "Fedor", 72);

		this.restTemplate.put("http://localhost:" + port + "/student", changedStudent);

		ResponseEntity<Void> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/faculty",
				HttpMethod.PUT, new HttpEntity<>(changedStudent), Void.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		Student student = this.restTemplate.getForObject(
				"http://localhost:" + port + "/student/" + changedStudent.getId(), Student.class);
		assertThat(changedStudent).isEqualTo(student);
	}
	@Test
	void testDeleteStudent() throws Exception {
		Student studentToDelete = new Student(123L, "Name1", 15);
		studentRepository.save(studentToDelete);

		Faculty deletedStudent = this.restTemplate.getForObject(
				"http://localhost:" + port + "/faculty/" + studentToDelete.getId(), Faculty.class);
		assertThat(deletedStudent.getId()).isNull();
		assertThat(deletedStudent.getName()).isNull();
		assertThat(deletedStudent.getColor()).isNull();
		ResponseEntity<Faculty> deletedResponseEntity = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/faculty/" + deletedStudent.getId(), Faculty.class);
		assertEquals(HttpStatus.BAD_REQUEST, deletedResponseEntity.getStatusCode());
	}




}
