package com.paintingscollectors.web;

import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.service.PaintingService;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.user.service.UserService;
import com.paintingscollectors.web.dto.LoginRequest;
import com.paintingscollectors.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class IndexController {

    private final UserService userService;
    private final PaintingService paintingService;

    @Autowired
    public IndexController(UserService userService,
                           PaintingService paintingService) {

        this.userService = userService;
        this.paintingService = paintingService;
    }

    @GetMapping("/")
    public ModelAndView getIndexPage() {

        ModelAndView modelAndView = new ModelAndView("index");

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("registerRequest", RegisterRequest.builder().build());
        modelAndView.setViewName("register");

        return modelAndView;
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.registerNewUser(registerRequest);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", LoginRequest.builder().build());

        return modelAndView;
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        User loggedInUser = userService.login(loginRequest);

        session.setAttribute("user_id",loggedInUser.getId());

        return "redirect:/home";
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        User loggedInUser = userService.getUserById(userId);

        List<Painting> allPaintings = paintingService.getAllPaintings();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("allPaintings", allPaintings);

        return modelAndView;
    }

    @GetMapping("/logout")
    public String getLogoutPage(HttpSession session) {

        session.invalidate();

        return "redirect:/home";
    }

}
