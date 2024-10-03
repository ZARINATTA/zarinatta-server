package com.zarinatta.zarinattaserver.bookmark.repository;

import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long>, BookMarkRepositoryCustom {
    @Query("SELECT b FROM BookMark b JOIN FETCH b.ticket WHERE b.ticket.id IN :ticketIds AND b.user= :user")
    List<BookMark> findAllByTicketIdInAndUserId(@Param("ticketIds") List<Long> ticketId, @Param("user") User user);
}
