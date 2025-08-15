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
            testService.prepareTest(req);
            req.getRequestDispatcher("/secure/testPassing.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/secure/tests");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        historyService.prepareResult(req);
        resp.sendRedirect(req.getContextPath() + "/secure/testResult");
    }
}