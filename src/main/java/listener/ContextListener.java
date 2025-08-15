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

        ObjectMapper mapper = createConfiguredMapper();

        File userFile = new File("C:\\Users\\User\\Desktop\\worrrk\\ThirdJRProject\\src\\main\\resources\\user.json");

        File testFile = new File("src/main/resources/tests");

        File historyFile = new File("C:\\Users\\User\\Desktop\\worrrk\\ThirdJRProject\\src\\main\\resources\\history.json");

        initServices(context, mapper, userFile, testFile, historyFile);
    }

    private ObjectMapper createConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    private void initServices(ServletContext context,
                              ObjectMapper mapper,
                              File userFile,
                              File testDir,
                              File historyFile) {

        CredentialsExtractor credentialsExtractor = new CredentialsExtractor();
        UserDao userDao = new UserDao(mapper, userFile);
        TestDao testDao = new TestDao(mapper, testDir);
        HistoryDao historyDao = new HistoryDao(mapper, historyFile);

        UserService userService = new UserService(userDao);
        AuthService authService = new AuthService(credentialsExtractor, userService);
        TestService testService = new TestService(testDao);
        HistoryService historyService = new HistoryService(historyDao, testService);
        ResultService resultService = new ResultService();

        context.setAttribute("credentialsExtractor", credentialsExtractor);
        context.setAttribute("userService", userService);
        context.setAttribute("authService", authService);
        context.setAttribute("testService", testService);
        context.setAttribute("historyService", historyService);
        context.setAttribute("resultService", resultService);
    }
}
