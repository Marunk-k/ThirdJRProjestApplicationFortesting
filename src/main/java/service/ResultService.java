package service;

import entity.TestResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResultService {
    public boolean prepareResult(HttpServletRequest req) {
        TestResult result = (TestResult) req.getSession().getAttribute("testResult");

        if (result == null) {
            return false;
        }

        setAttribute(req, result);
        return true;
    }

    private void setAttribute(HttpServletRequest req, TestResult result) {
        req.getSession().removeAttribute("testResult");
        req.setAttribute("testResult", result);
    }
}
