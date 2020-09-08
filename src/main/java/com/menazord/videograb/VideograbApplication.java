package com.menazord.videograb;

import com.menazord.videograb.forms.MainForm;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootApplication
public class VideograbApplication implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    public static void main(String[] args) {

        ApplicationContext ctx = new SpringApplicationBuilder(VideograbApplication.class)
                .headless(false).run(args);

        MainForm mainForm = new MainForm();
        mainForm.setApplicationContext(ctx);
        mainForm.run();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
