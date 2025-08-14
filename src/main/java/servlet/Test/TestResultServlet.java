package servlet.Test;

import entity.TestResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/secure/testResult")
public class TestResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TestResult result = (TestResult) req.getSession().getAttribute("testResult");

        if (result == null) {
            resp.sendRedirect(req.getContextPath() + "/secure/tests");
            return;
        }

        req.getSession().removeAttribute("testResult");

        req.setAttribute("testResult", result);
        req.getRequestDispatcher("/secure/testResult.jsp").forward(req, resp);
    }
}