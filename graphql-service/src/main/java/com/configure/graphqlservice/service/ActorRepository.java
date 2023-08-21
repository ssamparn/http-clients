package com.configure.graphqlservice.service;

import com.configure.graphqlservice.model.Actor;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActorRepository {

    private List<Actor> mockActors;
    private final MovieRepository movieRepository;

    public ActorRepository(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    private void initializeMockActors() {
        mockActors = new ArrayList<>(List.of(
                new Actor(1, "Keanu Reeves", List.of(movieRepository.getById(1), movieRepository.getById(2), movieRepository.getById(3))),
                new Actor(2, "Laurence Fishburne", List.of(movieRepository.getById(1), movieRepository.getById(2), movieRepository.getById(3))))
        );
    }

    public Actor getById(Integer id) {
        return mockActors
                .stream()
                .filter(actor -> actor.id() == (id))
                .findFirst()
                .orElse(null);
    }
}
