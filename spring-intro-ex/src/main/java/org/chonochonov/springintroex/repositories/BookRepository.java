package org.chonochonov.springintroex.repositories;

import org.chonochonov.springintroex.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Collection<Book> findAllByReleaseDateAfter(LocalDate localDate);
}
