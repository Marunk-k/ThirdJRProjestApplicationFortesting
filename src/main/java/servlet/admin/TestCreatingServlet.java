package servlet.admin;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.TestService;

import java.io.IOException;

@WebServlet(urlPatterns = "/secure/admin/testCreating")
public class TestCreatingServlet extends HttpServlet {
    private TestService testService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        testService = (TestService) config.getServletContext().getAttribute("testService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/secure/admin/testCreating.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        testService.saveNewTest(req);
        resp.sendRedirect(req.getContextPath() + "/secure/admin/adminMain");
    }
}
