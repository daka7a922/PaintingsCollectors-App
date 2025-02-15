package com.paintingscollectors.web;

import com.paintingscollectors.painting.service.PaintingService;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.user.service.UserService;
import com.paintingscollectors.web.dto.CreatePaintingRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/paintings")
public class PaintingController {


    private final UserService userService;
    private final PaintingService paintingService;

    @Autowired
    public PaintingController(UserService userService, PaintingService paintingService) {
        this.userService = userService;
        this.paintingService = paintingService;
    }

    @GetMapping("/new")
    public ModelAndView getNewPaintingPage(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        User loggedInUser = userService.getUserById(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("new-painting");
        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("createPaintingRequest", CreatePaintingRequest.builder().build());

        return modelAndView;
    }

    @PostMapping
    public ModelAndView createNewPainting(@Valid CreatePaintingRequest createPaintingRequest, BindingResult bindingResult, HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(userId);

        if (bindingResult.hasErrors()) {

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("new-painting");
            modelAndView.addObject("user", user);
            return modelAndView;
        }


        paintingService.createNewPainting(createPaintingRequest, user);

        return new ModelAndView("redirect:/home");
    }



}
