package com.zarinatta.zarinattaserver.bookmark;

import com.zarinatta.zarinattaserver.bookmark.dto.BookMarkCreateRequest;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.exception.exception.BookMarkNotFoundException;
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
                .orElseThrow(() -> new BookMarkNotFoundException("메서드 : createBookMark"));
        BookMark bookMark = BookMark.from(bookMarkCreateRequest, ticket, user);
        bookMarkRepository.save(bookMark);
    }
}
