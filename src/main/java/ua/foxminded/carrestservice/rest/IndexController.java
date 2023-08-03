package ua.foxminded.carrestservice.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class IndexController {

    @GetMapping
    String redirectToOpenApi() {
        return "redirect:/openapi";
    }
}
