package com.zarinatta.zarinattaserver.bookmark.controller;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.bookmark.dto.request.BookMarkCreateRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.request.BookMarkStatusUpdateRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.request.MyBookMarkRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.response.MyBookMarkPageResponse;
import com.zarinatta.zarinattaserver.bookmark.dto.response.MyBookMarkSearchResponse;
import com.zarinatta.zarinattaserver.bookmark.repository.BookMarkRepository;
import com.zarinatta.zarinattaserver.bookmark.service.BookMarkService;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.enums.BookMarkStatus;
import com.zarinatta.zarinattaserver.exception.exception.NotFound.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
public class BookMarkController {

    private final BookMarkRepository bookMarkRepository;
    private final BookMarkService bookMarkService;
    private final JwtService jwtService;

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<MyBookMarkSearchResponse> searchBookMark(HttpServletRequest request, @RequestParam("ticketIds") List<Long> ticketIds) {
        String accessToken = (String) request.getAttribute("accessToken");
        User user = jwtService.findUserByToken(accessToken)
                .orElseThrow(() -> new UserNotFoundException("searchBookMark"));
        List<BookMark> bookMarks = bookMarkRepository.findAllByTicketIdInAndUserId(ticketIds, user);
        List<MyBookMarkSearchResponse> response = bookMarks.stream()
                .map(bm -> MyBookMarkSearchResponse.builder()
                        .ticketId(bm.getTicket().getId())
                        .bookmarkId(bm.getId())
                        .build())
                .toList();

        return response;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createBookMark(HttpServletRequest request, @RequestBody @Valid BookMarkCreateRequest bookMarkCreateRequest) {
        String accessToken = (String) request.getAttribute("accessToken");
        User user = jwtService.findUserByToken(accessToken)
                .orElseThrow(() -> new UserNotFoundException("createBookMark"));
        bookMarkService.createBookMark(user, bookMarkCreateRequest);
        return "TicketId: " + bookMarkCreateRequest.getTicketId() + " - 즐겨찾기 추가 완료";
    }

    @DeleteMapping("/delete/{bookMarkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteBookMark(HttpServletRequest request, @PathVariable Long bookMarkId) {
        String accessToken = (String) request.getAttribute("accessToken");
        User user = jwtService.findUserByToken(accessToken)
                .orElseThrow(() -> new UserNotFoundException("deleteBookMark"));
        bookMarkService.deleteBookMark(user, bookMarkId);
        return "BookMarkId: " + bookMarkId + " - 즐겨찾기 삭제 완료";
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public MyBookMarkPageResponse getMyBookMarkList(HttpServletRequest request, @Valid MyBookMarkRequest myBookMarkRequest) {
        String accessToken = (String) request.getAttribute("accessToken");
        User user = jwtService.findUserByToken(accessToken)
                .orElseThrow(() -> new UserNotFoundException("getMyBookMarkList"));
        return bookMarkService.getMyBookMark(user, myBookMarkRequest);
    }

    @PostMapping("/status")
    @ResponseStatus(HttpStatus.CREATED)
    public BookMarkStatus updateBookMarkState(HttpServletRequest request, BookMarkStatusUpdateRequest body) {
        String accessToken = (String) request.getAttribute("accessToken");
        User user = jwtService.findUserByToken(accessToken)
                .orElseThrow(() -> new UserNotFoundException("updateBookMarkState"));
        log.info(user.getUserNick() + "님이 - BookMarkId : " + body.getBookMarkId() + "상태 변경 : " + body.getUpdateStatus());
        return bookMarkService.updateBookMarkStatus(body);
    }
}
