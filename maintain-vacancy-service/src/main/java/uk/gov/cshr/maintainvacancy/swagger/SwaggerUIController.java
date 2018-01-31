package uk.gov.cshr.maintainvacancy.swagger;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is responsible for ensuring that the Swagger UI html page is only accessible when the a specific profile is enabled on startup.
 */
@Profile("!dev")
@RestController
public class SwaggerUIController {
    /**
     * This method is responsible for preventing access to the swagger-ui.html page if a specific profile is not enabled.
     *
     * @param httpResponse
     * @throws IOException
     */
    @RequestMapping(value = "swagger-ui.html", method = RequestMethod.GET)
    public void getSwagger(HttpServletResponse httpResponse) throws IOException {
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
    }
}