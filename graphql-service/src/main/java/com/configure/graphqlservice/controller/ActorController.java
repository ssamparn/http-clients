package com.configure.graphqlservice.controller;

import com.configure.graphqlservice.model.Actor;
import com.configure.graphqlservice.service.ActorRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ActorController {

    private final ActorRepository actorRepository;

    public ActorController(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @QueryMapping
    public Actor actorById(@Argument Integer id) {
        return actorRepository.getById(id);
    }

}
