package com.example.spring31.repository;

import com.example.spring31.domain.entity.Author;
import com.example.spring31.domain.entity.Book;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    @Modifying
    @Query("update Book b set b.author = :author where b.id = :id")
    void updateAuthor(@Param("id") long bookId, @Param("author") Author author);

    @EntityGraph(attributePaths = {"author", "genres"})
    @NonNull
    List<Book> findAll();

    @Modifying
    @Query("update Book b set b.name = :name where b.id = :id")
    void updateNameById(@Param("id") long id, @Param("name") String name);
}
