package org.chonochonov.springintroex.services;

import org.chonochonov.springintroex.entities.Author;
import org.chonochonov.springintroex.entities.Book;

import java.io.IOException;
import java.util.Collection;

public interface AuthorService {
    void seedAuthors() throws IOException;
    long getAllAuthorsCount();
    Author findAuthorById(long id);
    Collection<Author> findAllAuthorsByCountOfBooks();
}
