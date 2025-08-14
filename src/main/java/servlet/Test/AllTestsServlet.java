package servlet.Test;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.TestService;

import java.io.IOException;

@WebServlet(urlPatterns = "/secure/tests")
public class AllTestsServlet extends HttpServlet {
    private TestService testService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        testService = (TestService) config.getServletContext().getAttribute("testService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String topicFilter = req.getParameter("topicFilter");

        testService.prepareTests(req, topicFilter);

        req.setAttribute("currentFilter", topicFilter);

        req.getRequestDispatcher("/secure/tests.jsp").forward(req, resp);
    }
}
