package com.safia.Service;

import com.safia.Entity.Student;
import com.safia.DAO.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

//tells spring that this is a service
@Service
public class StudentService {

    @Autowired
    @Qualifier("fakedata")
    private StudentDAO studentDAO;

    public Collection<Student> getAllStudents(){
        return studentDAO.getAllStudents();
    }

    public Student getStudentById(int id){
        return studentDAO.getStudentById(id);
    }

    public void removeStudentById(int id){
        this.studentDAO.removeStudentById(id);
    }

    public void updateStudent(Student student){
        this.studentDAO.updateStudent(student);
    }

    public void insertStudent(Student student) {
        studentDAO.insertStudentToDb(student);
    }
}
