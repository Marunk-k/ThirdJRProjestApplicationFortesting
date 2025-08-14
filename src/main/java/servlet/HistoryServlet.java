package servlet;

import entity.History;
import entity.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.HistoryService;
import service.TestService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/secure/history")
public class HistoryServlet extends HttpServlet {
    private HistoryService historyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        historyService = (HistoryService) config.getServletContext().getAttribute("historyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID userId = ((User)req.getSession().getAttribute("user")).getId();
        List<History> userHistory = historyService.findByUserId(userId);
        req.setAttribute("userHistory", userHistory);
        req.getRequestDispatcher("/secure/history.jsp").forward(req, resp);
    }
}
