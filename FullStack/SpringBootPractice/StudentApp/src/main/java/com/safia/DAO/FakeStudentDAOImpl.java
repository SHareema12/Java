package com.safia.DAO;

import com.safia.Entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

//this is a respository so spring will instantiate all beans for us
@Repository
@Qualifier("fakedata")
public class FakeStudentDAOImpl implements StudentDAO {

    @Autowired
    private static Map<Integer, Student> students;

    //statically enters data into the students map
    static {
        students = new HashMap<Integer, Student>(){

            {
                put(1,new Student(1, "Said", "Computer Science"));
                put(2,new Student(2, "Sal", "Finance"));
                put(3,new Student(3, "Sally", "Communication"));
            }

        };
    }

    @Override
    public Collection<Student> getAllStudents(){
        return this.students.values();
    }

    @Override
    public Student getStudentById(int id){
        return this.students.get(id);
    }

    @Override
    public void removeStudentById(int id) {
        this.students.remove(id);
    }

    @Override
    public void updateStudent(Student student){
        Student s = students.get(student.getId());
        s.setCourse(student.getCourse());
        s.setName(student.getName());

        students.put(student.getId(), student);
    }

    @Override
    public void insertStudentToDb(Student student) {
        this.students.put(student.getId(), student);
    }
}
