package org.chonochonov.springintroex.repositories;

import org.chonochonov.springintroex.entities.AgeRestriction;
import org.chonochonov.springintroex.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Collection<Book> findAllByReleaseDateAfter(LocalDate localDate);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Book AS b SET b.price = b.price * :multy WHERE b.copies < :copies")
    int increasePriceOfBooksWithCopiesLesserThan(@Param("multy") BigDecimal newPrice , @Param("copies") int copies);

    Collection<Book> findAllByCopiesLessThanEqualAndAgeRestrictionEqualsOrderByPrice(int copies, AgeRestriction ageRestriction);

}
