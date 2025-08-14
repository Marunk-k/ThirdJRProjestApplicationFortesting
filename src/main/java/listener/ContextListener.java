package listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dao.HistoryDao;
import dao.TestDao;
import dao.UserDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import service.*;
import util.CredentialsExtractor;

import java.io.File;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        ObjectMapper mapper = new ObjectMapper();
        File userFile = new File("C:\\Users\\User\\Desktop\\worrrk\\ThirdJRProject\\src\\main\\resources\\user.json");
        File testFile = new File("C:\\Users\\User\\Desktop\\worrrk\\ThirdJRProject\\src\\main\\resources\\test.json");
        File historyFile = new File("C:\\Users\\User\\Desktop\\worrrk\\ThirdJRProject\\src\\main\\resources\\history.json");
        context.setAttribute("mapper", mapper);
        context.setAttribute("userFile", userFile);
        context.setAttribute("testFile", userFile);

        CredentialsExtractor credentialsExtractor = new CredentialsExtractor();

//ПОМЕНЯТЬ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        UserDao userDao = new UserDao(mapper, userFile);
        UserService userService = new UserService(userDao);
        AuthService authService = new AuthService(credentialsExtractor, userService);

        TestDao testDao = new TestDao(mapper, testFile);

        TestService testService = new TestService(testDao);

        HistoryDao historyDao = new HistoryDao(mapper, historyFile);
        HistoryService historyService = new HistoryService(historyDao);

        context.setAttribute("userDao", userDao);
        context.setAttribute("userService", userService);
        context.setAttribute("credentialsExtractor", credentialsExtractor);
        context.setAttribute("authService", authService);
        context.setAttribute("testService", testService);
        context.setAttribute("historyService", historyService);




    }
}
