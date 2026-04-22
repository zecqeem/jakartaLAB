package org.example.jakartalabs.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jakartalabs.ejb.ApartmentService;
import org.example.jakartalabs.model.Apartment;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "LandlordServlet", value = "/manage")
public class LandlordServlet extends HttpServlet {

    @EJB
    private ApartmentService apartmentService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, Object> data = apartmentService.search(null, null, 0, Integer.MAX_VALUE);
        req.setAttribute("apartments", data.get("results"));
        req.getRequestDispatcher("/WEB-INF/jsp/manage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            int rooms = Integer.parseInt(req.getParameter("rooms"));
            int price = Integer.parseInt(req.getParameter("price"));

            if (rooms < 1 || price < 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Помилка: Некоректні значення кімнат або ціни.");
                return;
            }

            String title = req.getParameter("title");
            String desc  = req.getParameter("description");

            Apartment apt = new Apartment(0, title, rooms, price, desc);
            apartmentService.create(apt);

            resp.sendRedirect(req.getContextPath() + "/manage");

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Помилка: Ціна та кімнати мають бути числами.");
        } catch (jakarta.validation.ConstraintViolationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Помилка валідації: " + e.getConstraintViolations().iterator().next().getMessage());
        }
    }
}
