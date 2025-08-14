package servlet;

import entity.TestResult;
import entity.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.HistoryService;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/secure/myResults")
public class MyResultsServlet extends HttpServlet {
    private HistoryService historyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        historyService = (HistoryService) config.getServletContext().getAttribute("historyService");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID userId = ((User)req.getSession().getAttribute("user")).getId();
        List<TestResult> bestResults = historyService.extractBestResults(userId);
        req.setAttribute("bestResults", bestResults);
        req.getRequestDispatcher("/secure/myResults.jsp").forward(req, resp);
    }
}
