package com.configure.graphqlservice.model;

import java.util.List;

public record Actor(int id, String name, List<Movie> movies) {}
