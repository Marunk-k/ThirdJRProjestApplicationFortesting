package servlet.Test;

import entity.TestResult;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.HistoryService;
import service.TestService;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/secure/testPassing")
public class TestPassingServlet extends HttpServlet {
    private TestService testService;
    private HistoryService historyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        testService = (TestService) config.getServletContext().getAttribute("testService");
        historyService = (HistoryService) config.getServletContext().getAttribute("historyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UUID testId = UUID.fromString(req.getParameter("testId"));
            testService.prepareTestById(req, testId);
            req.getRequestDispatcher("/secure/testPassing.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/secure/tests");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TestResult result = testService.extractResult(req);
        historyService.saveResult(result, req);
        req.getSession().setAttribute("testResult", result);
        resp.sendRedirect(req.getContextPath() + "/secure/testResult");
    }
}