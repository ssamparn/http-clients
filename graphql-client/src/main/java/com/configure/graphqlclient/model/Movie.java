package com.configure.graphqlclient.model;

import java.util.List;

public record Movie(int id, String title, int year, List<String> genres, String director) {

}
