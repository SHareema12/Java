package com.safia.DAO;

import com.safia.Entity.Student;

import java.util.Collection;

public interface StudentDAO {
    Collection<Student> getAllStudents();

    Student getStudentById(int id);

    void removeStudentById(int id);

    void updateStudent(Student student);

    void insertStudentToDb(Student student);
}
