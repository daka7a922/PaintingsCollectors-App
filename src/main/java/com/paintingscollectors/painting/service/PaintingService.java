package com.paintingscollectors.painting.service;

import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.repository.PaintingRepository;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.user.service.UserService;
import com.paintingscollectors.web.dto.CreatePaintingRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class PaintingService {

    private final PaintingRepository paintingRepository;
    private final UserService userService;

    @Autowired
    public PaintingService(PaintingRepository paintingRepository,
                           UserService userService) {
        this.paintingRepository = paintingRepository;
        this.userService = userService;
    }

    public List<Painting> getAllPaintings() {

        List<Painting> all = paintingRepository.findAll();

        all.sort(Comparator.comparing(Painting::getVotes).reversed().thenComparing(Painting::getName));

        return all;
    }

    public void createNewPainting(CreatePaintingRequest createPaintingRequest, User loggedInUser) {

        Painting newPainting = Painting.builder()
                .name(createPaintingRequest.getName())
                .author(createPaintingRequest.getAuthor())
                .style(createPaintingRequest.getStyle())
                .imageUrl(createPaintingRequest.getImageUrl())
                .owner(loggedInUser)
                .votes(0)
                .build();

        paintingRepository.save(newPainting);
        log.info("New painting with id [%s] created".formatted(newPainting.getId()));
    }

}
