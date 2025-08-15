package servlet.admin;

import entity.Test;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.TestService;

import java.io.IOException;

@WebServlet(urlPatterns = "/secure/admin/testEdit")
public class TestEditingServlet extends HttpServlet {
    private TestService testService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        testService = (TestService) config.getServletContext().getAttribute("testService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        testService.prepareTest(req);
        req.getRequestDispatcher("/secure/admin/testEdit.jsp").forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        testService.updateTest(req);
        resp.sendRedirect( "/secure/admin/testManage");
    }
}
