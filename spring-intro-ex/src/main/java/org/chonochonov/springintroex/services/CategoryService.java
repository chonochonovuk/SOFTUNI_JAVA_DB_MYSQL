package org.chonochonov.springintroex.services;

import org.chonochonov.springintroex.entities.Category;

import java.io.IOException;


public interface CategoryService {
    void seedCategories() throws IOException;
    Category getCategoryById(long id);
}
