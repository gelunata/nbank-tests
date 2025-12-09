package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.containsInAnyOrder;

public class ResponseSpecs {
    private ResponseSpecs() {
    }

    private static ResponseSpecBuilder defaultRequestBuilder() {
        return new ResponseSpecBuilder();
    }

    public static ResponseSpecification entityWasCreated() {
        return defaultRequestBuilder()
                .expectStatusCode(HttpStatus.SC_CREATED)
                .build();
    }

    public static ResponseSpecification requestReturnsOK() {
        return defaultRequestBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    public static ResponseSpecification requestReturnsBadRequest() {
        return defaultRequestBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .build();
    }

    public static ResponseSpecification requestReturnsBadRequest(String errorKey, String[] errorValue) {
        return defaultRequestBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectBody(errorKey, containsInAnyOrder(errorValue))
                .build();
    }

    public static ResponseSpecification requestReturnsForbidden() {
        return defaultRequestBuilder()
                .expectStatusCode(HttpStatus.SC_FORBIDDEN)
                .build();
    }
}
