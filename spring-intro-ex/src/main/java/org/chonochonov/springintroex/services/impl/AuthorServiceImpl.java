package org.chonochonov.springintroex.services.impl;

import org.chonochonov.springintroex.constants.GlobalConstants;
import org.chonochonov.springintroex.entities.Author;
import org.chonochonov.springintroex.entities.Book;
import org.chonochonov.springintroex.repositories.AuthorRepository;
import org.chonochonov.springintroex.services.AuthorService;
import org.chonochonov.springintroex.utilis.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final FileUtil fileUtil;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }


    @Override
    public void seedAuthors() throws IOException {
        if (this.authorRepository.count() != 0){
            return;
        }

        String[] fileContent = this.fileUtil.readFileContent(GlobalConstants.AUTHORS_FILE_PATH);

        Arrays.stream(fileContent).forEach(x -> {
            String[] params = x.split("\\s+");
            Author author = new Author();
            author.setFirstName(params[0]);
            author.setLastName(params[1]);
            this.authorRepository.saveAndFlush(author);
        });

    }

    @Override
    public long getAllAuthorsCount() {
        return this.authorRepository.count();
    }

    @Override
    public Author findAuthorById(long id) {
        return this.authorRepository.getOne(id);
    }

    @Override
    public Collection<Author> findAllAuthorsByCountOfBooks() {
        return this.authorRepository.findAuthorsByCountOfBooks();
    }
}
