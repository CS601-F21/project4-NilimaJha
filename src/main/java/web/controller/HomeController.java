package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @GetMapping("/index")
    public String index(Model model) {
        System.out.println("inside /index");
        model.addAttribute("name", "Nilima Jha");
        return "index.html";
    }
    @GetMapping("/indexhome/{name}")
    public String indexHome(@PathVariable("name") String name, Model model) {
        System.out.println("inside /indexhome");
        model.addAttribute("name", name);
        return "home.html";
    }
}
