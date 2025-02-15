package com.paintingscollectors.painting.service;

import com.paintingscollectors.exception.DomainException;
import com.paintingscollectors.painting.model.FavouritePainting;
import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.repository.FavouritePaintingRepository;
import com.paintingscollectors.painting.repository.PaintingRepository;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.user.service.UserService;
import com.paintingscollectors.web.dto.CreatePaintingRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PaintingService {

    private final PaintingRepository paintingRepository;
    private final UserService userService;
    private final FavouritePaintingRepository favouritePaintingRepository;

    @Autowired
    public PaintingService(PaintingRepository paintingRepository,
                           UserService userService,
                           FavouritePaintingRepository favouritePaintingRepository) {
        this.paintingRepository = paintingRepository;
        this.userService = userService;
        this.favouritePaintingRepository = favouritePaintingRepository;
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

    public void createFavouritePainting(UUID id, User user) {

        Painting painting = getById(id);

        boolean isAlreadyFavourite = user.getFavouritePaintings()
                .stream()
                .anyMatch(fp -> fp.getName().equals(painting.getName()) && fp.getAuthor().equals(painting.getAuthor()));
        if (isAlreadyFavourite) {
            return;
        }

        FavouritePainting favouritePainting = FavouritePainting.builder()
                .name(painting.getName())
                .author(painting.getAuthor())
                .owner(user)
                .imageUrl(painting.getImageUrl())
                .createdOn(LocalDateTime.now())
                .build();

        favouritePaintingRepository.save(favouritePainting);
    }

    private Painting getById(UUID id) {

        return paintingRepository.findById(id).orElseThrow(()-> new DomainException(String.format("Painting with id [%s] not found", id)));

    }

    public void deleteFavouritePainting(UUID id) {

        favouritePaintingRepository.deleteById(id);
        log.info("Favourite painting with id [%s] removed from favourites".formatted(id));

    }

    public void deletePainting(UUID id) {

        paintingRepository.deleteById(id);
        log.info("Painting with id [%s] removed".formatted(id));
    }

    public void votePainting(UUID id) {

        Painting painting = getById(id);
        painting.setVotes(painting.getVotes() + 1);

        paintingRepository.save(painting);
    }
}
