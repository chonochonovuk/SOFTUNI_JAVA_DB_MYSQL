package org.chonochonov.springintroex.services.impl;

import org.chonochonov.springintroex.constants.GlobalConstants;
import org.chonochonov.springintroex.entities.*;
import org.chonochonov.springintroex.repositories.BookRepository;
import org.chonochonov.springintroex.services.AuthorService;
import org.chonochonov.springintroex.services.BookService;
import org.chonochonov.springintroex.services.CategoryService;
import org.chonochonov.springintroex.utilis.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final FileUtil fileUtil;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService, FileUtil fileUtil) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() != 0){
            return;
        }

        String[] fileContent = this.fileUtil.readFileContent(GlobalConstants.BOOKS_FILE_PATH);

         Arrays.stream(fileContent)
                 .forEach(f -> {
                     String[] params = f.split("\\s+");
                     Book book = new Book();
                     Author author = this.getRandomAuthor();
                     EditionType editionType = EditionType.values()[Integer.parseInt(params[0])];
                     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                     LocalDate localDate = LocalDate.parse(params[1],formatter);
                     int copies = Integer.parseInt(params[2]);
                     BigDecimal price = new BigDecimal(params[3]);
                     AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(params[4])];
                     String title = this.getTitle(params);
                     Set<Category> categories = this.getRandomCategories();
                     book.setAuthor(author);
                     book.setEditionType(editionType);
                     book.setReleaseDate(localDate);
                     book.setCopies(copies);
                     book.setPrice(price);
                     book.setAgeRestriction(ageRestriction);
                     book.setTitle(title);
                     book.setCategories(categories);
                     this.bookRepository.saveAndFlush(book);
                 });
    }

    @Override
    public Collection<Book> getBooksByReleaseDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate localDate = LocalDate.parse("31/12/2000",formatter);


        return this.bookRepository.findAllByReleaseDateAfter(localDate);
    }

    @Override
    public void setNewPrice() {
        this.bookRepository.increasePriceOfBooksWithCopiesLesserThan(new BigDecimal("1.10"),1000);
    }

    @Override
    public Collection<Book> getBooksByCopiesAndAge() {
       return this.bookRepository.findAllByCopiesLessThanEqualAndAgeRestrictionEqualsOrderByPrice(10000,AgeRestriction.ADULT);
    }

    private Set<Category> getRandomCategories() {
        Set<Category> random = new HashSet<>();
        Random random1 = new Random();
        int bound = random1.nextInt(3) + 1;
        for (int i = 1; i <= bound; i++) {
            int ids = random1.nextInt(8) + 1;
            random.add(this.categoryService.getCategoryById(ids));
        }
        return random;
    }

    private String getTitle(String[] params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 5; i < params.length; i++) {
            sb.append(params[i]).append(" ");
        }
        return sb.toString().trim();
    }

    private Author getRandomAuthor(){
        Random random = new Random();
        long randomId = random.nextInt((int) this.authorService.getAllAuthorsCount()) + 1;

        return this.authorService.findAuthorById(randomId);
    }
}
