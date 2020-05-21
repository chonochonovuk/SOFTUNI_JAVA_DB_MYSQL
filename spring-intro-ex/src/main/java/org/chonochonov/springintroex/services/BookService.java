package org.chonochonov.springintroex.services;

import org.chonochonov.springintroex.entities.Book;

import java.io.IOException;
import java.util.Collection;

public interface BookService {
    void seedBooks() throws IOException;
    Collection<Book> getBooksByReleaseDate();
    void setNewPrice();
    Collection<Book> getBooksByCopiesAndAge();
}
