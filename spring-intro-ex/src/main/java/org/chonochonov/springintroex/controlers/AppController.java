package org.chonochonov.springintroex.controlers;

import org.chonochonov.springintroex.entities.Author;
import org.chonochonov.springintroex.entities.Book;
import org.chonochonov.springintroex.services.AuthorService;
import org.chonochonov.springintroex.services.BookService;
import org.chonochonov.springintroex.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;


@Controller
public class AppController implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AppController(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;



    }

    @Override
    public void run(String... args) throws Exception {
        // Seed Data
        this.categoryService.seedCategories();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();

        //Ex. 1
        // List<Book> books = new ArrayList<>(this.bookService.getBooksByReleaseDate());

        //Ex. 2
//        for (Author author : this.authorService.findAllAuthorsByCountOfBooks()) {
//            System.out.println(author.getFirstName() + " " + author.getLastName() + " books size --->>> " + author.getBooks().size());
//        }
        for (Book book : this.bookService.getBooksByCopiesAndAge()) {
            System.out.printf("%s %s %d %.2f%n",book.getTitle(),book.getAuthor().getFirstName(),book.getCopies(),book.getPrice());
        }
    }
}
