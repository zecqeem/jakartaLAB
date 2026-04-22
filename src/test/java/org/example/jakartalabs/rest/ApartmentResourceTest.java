package org.example.jakartalabs.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Apartment REST API — демо-тести")
class ApartmentResourceTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/jakartalabs_war_exploded/api";
    }

    @Test
    @DisplayName("GET /apartments → 200 і повертає поле results")
    void getAllApartments_returns200() {
        given()
            .when()
                .get("/apartments")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("results", notNullValue())
                .body("total",   greaterThan(0));
    }

    @Test
    @DisplayName("GET /apartments?rooms=2 → фільтрує тільки 2-кімнатні")
    void filterByRooms_returnsOnlyMatchingApartments() {
        given()
            .queryParam("rooms", 2)
            .when()
                .get("/apartments")
            .then()
                .statusCode(200)
                .body("results.rooms", everyItem(equalTo(2)));
    }

    @Test
    @DisplayName("GET /apartments?rooms=1&maxPrice=10000 → комбо-фільтр")
    void filterByRoomsAndMaxPrice() {
        given()
            .queryParam("rooms",    1)
            .queryParam("maxPrice", 10000)
            .when()
                .get("/apartments")
            .then()
                .statusCode(200)
                .body("results.rooms",  everyItem(equalTo(1)))
                .body("results.price",  everyItem(lessThanOrEqualTo(10000)));
    }

    @Test
    @DisplayName("GET /apartments?page=0&size=1 → пагінація, max 1 результат")
    void pagination_returnsOneResult() {
        given()
            .queryParam("page", 0)
            .queryParam("size", 1)
            .when()
                .get("/apartments")
            .then()
                .statusCode(200)
                .body("results.size()", lessThanOrEqualTo(1))
                .body("page", equalTo(0))
                .body("size", equalTo(1));
    }

    @Test
    @DisplayName("GET /apartments/1 → 200 з коректним об'єктом")
    void getById_existsReturns200() {
        given()
            .when()
                .get("/apartments/1")
            .then()
                .statusCode(200)
                .body("id",    equalTo(1))
                .body("title", notNullValue());
    }

    @Test
    @DisplayName("GET /apartments/999 → 404")
    void getById_notFoundReturns404() {
        given()
            .when()
                .get("/apartments/999")
            .then()
                .statusCode(404)
                .body("error", containsString("999"));
    }

    @Test
    @DisplayName("POST /apartments → 201 Created з коректними даними")
    void create_validApartment_returns201() {
        String body = """
                {
                  "title": "Тестова квартира",
                  "rooms": 2,
                  "price": 12000,
                  "description": "REST Assured тест"
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
                .post("/apartments")
            .then()
                .statusCode(201)
                .body("id",    greaterThan(0))
                .body("title", equalTo("Тестова квартира"))
                .body("price", equalTo(12000));
    }

    @Test
    @DisplayName("POST /apartments → 400: назва порожня")
    void create_blankTitle_returns400() {
        String body = """
                {
                  "title": "",
                  "rooms": 2,
                  "price": 10000,
                  "description": "Без назви"
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
                .post("/apartments")
            .then()
                .statusCode(400)
                .body("messages", hasItem(containsString("порожньою")));
    }

    @Test
    @DisplayName("POST /apartments → 400: @ValidPrice ціna не кратна 100")
    void create_priceNotDivisibleBy100_returns400() {
        String body = """
                {
                  "title": "Погана ціна",
                  "rooms": 1,
                  "price": 9999,
                  "description": "Тест кастомного валідатора"
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
                .post("/apartments")
            .then()
                .statusCode(400)
                .body("messages", hasItem(containsString("кратна 100")));
    }

    @Test
    @DisplayName("POST /apartments → 400: @ValidApartment — 3 кімнати, ціна < 10000")
    void create_threeRoomsLowPrice_returns400() {
        String body = """
                {
                  "title": "Дешева трьохкімнатна",
                  "rooms": 3,
                  "price": 5000,
                  "description": "Тест класового валідатора"
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
                .post("/apartments")
            .then()
                .statusCode(400)
                .body("messages", hasItem(containsString("10000")));
    }

    @Test
    @DisplayName("PUT /apartments/1 → 200 і дані оновлено")
    void update_validData_returns200() {
        String body = """
                {
                  "title": "Оновлена квартира",
                  "rooms": 2,
                  "price": 15000,
                  "description": "Оновлено через REST Assured"
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
                .put("/apartments/1")
            .then()
                .statusCode(200)
                .body("title", equalTo("Оновлена квартира"))
                .body("price", equalTo(15000));
    }

    @Test
    @DisplayName("PUT /apartments/999 → 404 не знайдено")
    void update_notFound_returns404() {
        String body = """
                {
                  "title": "Нема такої",
                  "rooms": 1,
                  "price": 5000,
                  "description": ""
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
                .put("/apartments/999")
            .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE /apartments/{id} → 204 No Content (свій створений)")
    void delete_exists_returns204() {
        String body = """
                {
                  "title": "Квартира для видалення",
                  "rooms": 1,
                  "price": 5000,
                  "description": "Буде видалена"
                }
                """;

        int createdId = given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
                .post("/apartments")
            .then()
                .statusCode(201)
                .extract().path("id");

        given()
            .when()
                .delete("/apartments/" + createdId)
            .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("DELETE /apartments/999 → 404")
    void delete_notFound_returns404() {
        given()
            .when()
                .delete("/apartments/999")
            .then()
                .statusCode(404);
    }
}
