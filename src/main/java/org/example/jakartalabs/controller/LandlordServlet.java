package org.example.jakartalabs.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jakartalabs.model.Apartment;
import org.example.jakartalabs.model.MockDatabase;
import java.io.IOException;

@WebServlet(name = "LandlordServlet", value = "/manage")
public class LandlordServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setAttribute("apartments", MockDatabase.apartments);
    req.getRequestDispatcher("/WEB-INF/jsp/manage.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      int rooms = Integer.parseInt(req.getParameter("rooms"));
      int price = Integer.parseInt(req.getParameter("price"));

      if (rooms < 1 || price < 0) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Помилка: Некоректні значення кімнат або ціни.");
        return;
      }

      int id = MockDatabase.apartments.size() + 1;
      String title = req.getParameter("title");
      String desc = req.getParameter("description");

      MockDatabase.apartments.add(new Apartment(id, title, rooms, price, desc));
      resp.sendRedirect(req.getContextPath() + "/manage");

    } catch (NumberFormatException e) {
      // Якщо замість числа прислали текст
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Помилка: Ціна та кімнати мають бути числами.");
    }
  }
}
