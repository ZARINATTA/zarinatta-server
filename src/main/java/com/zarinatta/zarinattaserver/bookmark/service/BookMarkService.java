package com.zarinatta.zarinattaserver.bookmark.service;

import com.zarinatta.zarinattaserver.bookmark.dto.request.BookMarkCreateRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.request.BookMarkStatusUpdateRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.request.MyBookMarkRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.response.MyBookMarkPageResponse;
import com.zarinatta.zarinattaserver.bookmark.dto.response.MyBookMarkResponse;
import com.zarinatta.zarinattaserver.bookmark.repository.BookMarkRepository;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.enums.BookMarkStatus;
import com.zarinatta.zarinattaserver.exception.exception.NotFound.BookMarkNotFoundException;
import com.zarinatta.zarinattaserver.exception.exception.NotFound.TicketNotFoundException;
import com.zarinatta.zarinattaserver.exception.exception.NotPermit.BookMarkPermitException;
import com.zarinatta.zarinattaserver.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final TicketRepository ticketRepository;

    /**
     * 즐겨찾기 생성 - 하루 최대 3개까지 가능
     * @param user
     * @param bookMarkCreateRequest
     */
    @Transactional
    public void createBookMark(User user, BookMarkCreateRequest bookMarkCreateRequest) {
        Ticket ticket = ticketRepository.findById(bookMarkCreateRequest.ticketId())
                .orElseThrow(() -> new TicketNotFoundException("createBookMark"));
        LocalDate targetDate = LocalDate.now();
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.atTime(LocalTime.MAX);
        List<BookMark> todayUserBookMarked = bookMarkRepository.findByUserAndCreatedToday(user, start, end);
        if (todayUserBookMarked.size() >= 3) {
            throw new BookMarkPermitException("createBookMark", "최대 하루에 3개까지 즐겨찾기가 가능합니다 :)");
        }
        BookMark bookMark = BookMark.from(bookMarkCreateRequest, ticket, user);
        bookMarkRepository.save(bookMark);
    }

    /**
     * 즐겨찾기 삭제 - soft delete
     * @param user
     * @param bookMarkId
     */
    @Transactional
    public void deleteBookMark(User user, Long bookMarkId) {
        BookMark bookMark = bookMarkRepository.findById(bookMarkId)
                .orElseThrow(() -> new BookMarkNotFoundException("deleteBookMark"));
        if (!bookMark.getUser().getId().equals(user.getId())) {
            throw new BookMarkPermitException("deleteBookMark", "userID : " + user.getId() + " - bookMarkID : " + bookMark.getId() + " 삭제 불가");
        }
        bookMark.delete();
    }

    /**
     * 즐겨찾기 조회 - 내 즐겨찾기 탭
     */
    public MyBookMarkPageResponse getMyBookMark(User user, MyBookMarkRequest myBookMarkRequest) {
        Page<BookMark> myBookMarkByRequest = bookMarkRepository.findMyBookMarkByRequest(user, myBookMarkRequest);
        List<MyBookMarkResponse> content = myBookMarkByRequest.getContent().stream()
                .map(MyBookMarkResponse::from)
                .collect(Collectors.toList());

        return MyBookMarkPageResponse.builder()
                .responseList(content)
                .page(myBookMarkByRequest.getNumber() + 1)
                .totalDataCount(myBookMarkByRequest.getTotalElements())
                .totalPageCount(myBookMarkByRequest.getTotalPages())
                .build();
    }

    /**
     * 즐겨찾기 상태 업데이트 - SUCCESS, FAIL, UNKNOWN
     * @param body
     * @return
     */
    @Transactional
    public BookMarkStatus updateBookMarkStatus(BookMarkStatusUpdateRequest body){
        BookMark bookMark = bookMarkRepository.findById(body.bookMarkId())
                .orElseThrow(() -> new BookMarkNotFoundException("updateBookMarkStatus"));
        return bookMark.updateStatus(body.updateStatus());
    }
}
