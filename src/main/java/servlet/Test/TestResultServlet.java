package servlet.Test;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ResultService;

import java.io.IOException;

@WebServlet("/secure/testResult")
public class TestResultServlet extends HttpServlet {
    ResultService resultService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        resultService = (ResultService) config.getServletContext().getAttribute("resultService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (resultService.prepareResult(req)) {
            req.getRequestDispatcher("/secure/testResult.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/secure/tests");
        }
    }
}