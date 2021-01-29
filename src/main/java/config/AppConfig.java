package config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import service.IStudentService;
import service.StudentService;

import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan("controller")
@PropertySource("classpath:uploadfile.properties")
public class AppConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    @Bean
    public SpringResourceTemplateResolver resourceTemplateResolver(){
        SpringResourceTemplateResolver resourceTemplateResolver=new SpringResourceTemplateResolver();
        resourceTemplateResolver.setApplicationContext(applicationContext);
        resourceTemplateResolver.setPrefix("/WEB-INF/views/");
        resourceTemplateResolver.setSuffix(".html");
        resourceTemplateResolver.setCharacterEncoding("UTF-8");
        resourceTemplateResolver.setTemplateMode(TemplateMode.HTML);
        return resourceTemplateResolver;
    }
    @Bean
    public TemplateEngine templateEngine(){
        TemplateEngine templateEngine=new TemplateEngine();
        templateEngine.setTemplateResolver(resourceTemplateResolver());
        return templateEngine;
    }
    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver=new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
    @Bean
    public IStudentService instanceIStudentService(){
        return new StudentService();
    }

    //////////////////////////////// Config upload
    //////Apache Common Upload
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getResolver() throws IOException{
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        //set the maximum size
        resolver.setMaxUploadSizePerFile(5242880);
        // you may also set other available
        return resolver;
    }
    @Autowired
    Environment env;
    // Cau hinh su dung file nguon tinh
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String fileUpload=env.getProperty("file_upload");
//        Image resource
        registry.addResourceHandler("/i/**")
                .addResourceLocations("file:"+fileUpload);
        /// duong dan den "file"
    }
    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
        // using email
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("luonganhtu666@gmail.com");
        mailSender.setPassword("luonganhtu15051995");

        Properties javaMailProperties=new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable","true");
        javaMailProperties.put("mail.smtp.auth","true");
        javaMailProperties.put("mail.transport.protocol","smtp");
        javaMailProperties.put("mail.debug","true");

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }
}
