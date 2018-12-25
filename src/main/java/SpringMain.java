import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {

//получаем контекст спринга, указывая путь к конфигурационному файлу
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml")) {

//можем вывести все бины на печать
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));



        }
    }

}
