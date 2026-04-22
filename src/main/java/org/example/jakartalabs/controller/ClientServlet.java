package org.example.jakartalabs.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jakartalabs.ejb.ApartmentService;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ClientServlet", value = "/search")
public class ClientServlet extends HttpServlet {

    @EJB
    private ApartmentService apartmentService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String roomsParam    = req.getParameter("rooms");
        String maxPriceParam = req.getParameter("maxPrice");

        Integer rooms    = (roomsParam    != null && !roomsParam.isBlank())
                ? Integer.parseInt(roomsParam) : null;
        Integer maxPrice = (maxPriceParam != null && !maxPriceParam.isBlank())
                ? Integer.parseInt(maxPriceParam) : null;

        Map<String, Object> data = apartmentService.search(rooms, maxPrice, 0, Integer.MAX_VALUE);

        req.setAttribute("apartments", data.get("results"));
        req.setAttribute("total",      data.get("total"));
        req.getRequestDispatcher("/WEB-INF/jsp/client.jsp").forward(req, resp);
    }
}