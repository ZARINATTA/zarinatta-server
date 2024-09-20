package com.zarinatta.zarinattaserver.bookmark;

import com.zarinatta.zarinattaserver.bookmark.dto.BookMarkCreateRequest;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.exception.exception.NotFound.BookMarkNotFoundException;
import com.zarinatta.zarinattaserver.exception.exception.NotFound.TicketNotFoundException;
import com.zarinatta.zarinattaserver.exception.exception.NotPermit.BookMarkPermitException;
import com.zarinatta.zarinattaserver.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void createBookMark(User user, BookMarkCreateRequest bookMarkCreateRequest) {
        Ticket ticket = ticketRepository.findById(bookMarkCreateRequest.getTicketId())
                .orElseThrow(() -> new TicketNotFoundException("createBookMark"));
        BookMark bookMark = BookMark.from(bookMarkCreateRequest, ticket, user);
        bookMarkRepository.save(bookMark);
    }

    @Transactional
    public void deleteBookMark(User user, Long bookMarkId) {
        BookMark bookMark = bookMarkRepository.findById(bookMarkId)
                .orElseThrow(() -> new BookMarkNotFoundException("deleteBookMark"));
        if (!bookMark.getUser().getId().equals(user.getId())) {
            throw new BookMarkPermitException("deleteBookMark", "userID : " + user.getId() + " - bookMarkID : " + bookMark.getId() + " 삭제 불가");
        }
        bookMarkRepository.delete(bookMark);
    }
}
