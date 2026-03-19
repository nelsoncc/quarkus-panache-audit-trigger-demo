package io.github.nelsoncc.auditdemo.product;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ProductResourceTest {

    @Test
    void shouldCreateProductWithAuditColumnsFilledByTrigger() {
        given().contentType(ContentType.JSON)
                .header("X-Audit-User", "ada")
                .body("""
				{
				  "name": "create-audit-demo"
				}
				""")
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .body("name", equalTo("create-audit-demo"))
                .body("archived", equalTo(false))
                .body("createdAt", notNullValue())
                .body("createdBy", equalTo("ada"))
                .body("updatedAt", notNullValue())
                .body("updatedBy", equalTo("ada"));
    }

    @Test
    void shouldUpdateAuditColumnsForManagedAndBulkPaths() {
        int id = given().contentType(ContentType.JSON)
                .header("X-Audit-User", "ada")
                .body("""
				{
				  "name": "tmp-managed-bulk-demo"
				}
				""")
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given().contentType(ContentType.JSON)
                .header("X-Audit-User", "bob")
                .body("""
				{
				  "newName": "tmp-managed-bulk-demo-renamed"
				}
				""")
                .when()
                .put("/products/{id}/rename-managed", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("updatedBy", equalTo("bob"))
                .body("updatedAt", notNullValue());

        given().contentType(ContentType.JSON)
                .header("X-Audit-User", "carol")
                .body("""
				{
				  "prefix": "tmp-"
				}
				""")
                .when()
                .post("/products/archive/panache")
                .then()
                .statusCode(200)
                .body("affectedRows", greaterThanOrEqualTo(1))
                .body("products.findAll { it.id == %s }[0].archived".formatted(id), equalTo(true))
                .body("products.findAll { it.id == %s }[0].updatedBy".formatted(id), equalTo("carol"));

        given().contentType(ContentType.JSON)
                .header("X-Audit-User", "dave")
                .body("""
				{
				  "prefix": "tmp-"
				}
				""")
                .when()
                .post("/products/unarchive/native")
                .then()
                .statusCode(200)
                .body("affectedRows", greaterThanOrEqualTo(1))
                .body("products.findAll { it.id == %s }[0].archived".formatted(id), equalTo(false))
                .body("products.findAll { it.id == %s }[0].updatedBy".formatted(id), equalTo("dave"));
    }

    @Test
    void shouldFallbackToSystemWhenNoAuditHeaderIsProvided() {
        given().contentType(ContentType.JSON)
                .body("""
				{
				  "name": "system-user-demo"
				}
				""")
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .body("createdBy", equalTo("system"))
                .body("updatedBy", equalTo("system"));
    }
}
