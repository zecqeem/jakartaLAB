package org.example.jakartalabs;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html;charset=UTF-8");

    String section = req.getParameter("section");
    String content = "Інформація відсутня. Оберіть розділ.";

    if ("skills".equals(section)) {
      content = "Стек: Java, Еластіко, діагональ на 30 метрів.";
    }

    try (PrintWriter out = resp.getWriter()) {
      out.println("<html>");
      out.println("<head><title>Деталі профілю</title></head>");
      out.println("<body>");
      out.println("<h2>Детальна інформація:</h2>");
      out.println("<p>" + content + "</p>");
      out.println("<br><a href='index.html'>Повернутися на головну</a>");
      out.println("</body>");
      out.println("</html>");
    }
  }
}