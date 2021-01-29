package controller;

import model.Student;
import model.StudentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.IStudentService;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private IStudentService iStudentService;
    @Autowired
    Environment env;
    @Autowired
    MailSender mailSender;
    @GetMapping
    public ModelAndView index(){
        ModelAndView index=new ModelAndView("index");
        index.addObject("students",iStudentService.findAll());
        return index;
    }
    @GetMapping("/create")
    public ModelAndView createForm(){
        ModelAndView create= new ModelAndView("create","s", new StudentForm());
        return create;
    }
    @PostMapping("/create")
    public ModelAndView createNewStudent(@ModelAttribute StudentForm s) throws IOException{
        ModelAndView modelAndView= new ModelAndView("create");
        MultipartFile file=s.getAvatar();

        String avatar=file.getOriginalFilename();
        String thu_muc= env.getProperty("file_upload").toString();
        FileCopyUtils.copy(file.getBytes(), new File(thu_muc+avatar));

        Student s1=new Student(s.getName(),s.getAddress(),avatar);
        iStudentService.save(s1);
        modelAndView.addObject("s",new StudentForm());
        modelAndView.addObject("mess","tthanh cong");
        sendEmail("luonganhtu666@gmail.com","ntnghia199x@gmail.com","test","Tu dep zai");
        return modelAndView;
    }
    public void sendEmail(String from,String to, String subject,String content){
        SimpleMailMessage mailMessage= new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailSender.send(mailMessage);
    }
}
