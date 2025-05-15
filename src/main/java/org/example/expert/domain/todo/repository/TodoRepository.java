package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query(""" 
            select t 
            from Todo t 
            where(:startDate is null or date(t.modifiedAt) >= :startDate)
            and (:endDate is null or date(t.modifiedAt) <= :endDate) order by t.modifiedAt desc
            """)
    Page<Todo> findAllByOrderByModifiedAtDesc(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate,
                                              Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    @EntityGraph(attributePaths = {"user"})
    @Query("""
            select t from Todo t 
            where (:weather is null or t.weather =: weather)
            and (:startDate is null or date(t.modifiedAt) >= :startDate)
            and (:endDate is null or date(t.modifiedAt) <= :endDate) order by t.modifiedAt desc
            """)
    Page<Todo> findAllByWeatherOrderByModifiedAtDesc(@Param("weather") String weather,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate,
                                                     Pageable pageable);
}
