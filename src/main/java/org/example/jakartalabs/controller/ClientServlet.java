package org.example.jakartalabs.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jakartalabs.model.Apartment;
import org.example.jakartalabs.model.MockDatabase;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "ClientServlet", value = "/search")
public class ClientServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String roomsParam = req.getParameter("rooms");
    List<Apartment> result = MockDatabase.apartments;

    if (roomsParam != null && !roomsParam.isEmpty()) {
      int rooms = Integer.parseInt(roomsParam);
      result = result.stream()
              .filter(a -> a.getRooms() == rooms)
              .collect(Collectors.toList());
    }

    req.setAttribute("apartments", result);
    req.getRequestDispatcher("/WEB-INF/jsp/client.jsp").forward(req, resp);
  }
}