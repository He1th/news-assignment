package example.micronaut.controllers;

import example.micronaut.services.NewsService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;

import java.io.IOException;

@Controller("/")
public class IndexController {

    @Inject
    NewsService newsService;

    @Get("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String index() throws IOException {
        return newsService.findNews();
    }
}