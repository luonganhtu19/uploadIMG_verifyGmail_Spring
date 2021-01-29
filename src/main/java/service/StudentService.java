package service;

import model.Student;

import java.util.*;

public class StudentService implements IStudentService {
    private static Map<Long,Student> listStudent;
    static {
        listStudent=new HashMap<>();
        listStudent.put(1L, new Student(1l,"thanhPH","HN"));
        listStudent.put(2L, new Student(2l,"thanhPH1","HP"));
        listStudent.put(3L, new Student(3l,"thanhP1H","HNP"));
        listStudent.put(4L, new Student(4l,"thanhPH","HN"));
    }
    @Override
    public List<Student> findAll() {
        ArrayList list=new ArrayList(listStudent.values());
        return list;
    }

    @Override
    public Student findById(long id) {
        return null;
    }

    @Override
    public void update(Student model) {

    }

    @Override
    public void save(Student model) {
        Long stt=findMaxId()+1L;
        model.setId(stt);
        listStudent.put(stt,model);
    }
    private long findMaxId(){
     long maxID=  Collections.max(listStudent.entrySet(), Map.Entry.comparingByKey()).getKey();
     return maxID;
    }

    @Override
    public void remove(long id) {

    }
}
