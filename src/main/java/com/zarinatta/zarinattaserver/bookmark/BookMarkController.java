package com.zarinatta.zarinattaserver.bookmark;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.bookmark.dto.request.BookMarkCreateRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.response.BookMarkSearchResponse;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.exception.exception.NotFound.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.zarinatta.zarinattaserver.enums.Values.HEADER_AUTHORIZATION;

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
    public List<BookMarkSearchResponse> searchBookMark(@RequestHeader(value = HEADER_AUTHORIZATION) String authorizationHeader, @RequestParam("ticketIds") List<Long> ticketIds) {
        User user = jwtService.findUserByToken(authorizationHeader)
                .orElseThrow(() -> new UserNotFoundException("searchBookMark"));
        List<BookMark> bookMarks = bookMarkRepository.findAllByTicketIdInAndUserId(ticketIds, user);
        List<BookMarkSearchResponse> response = bookMarks.stream()
                .map(bookMark -> BookMarkSearchResponse.of(bookMark.getTicket().getId(), true))
                .collect(Collectors.toList());
        return response;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createBookMark(@RequestHeader(value = HEADER_AUTHORIZATION) String authorizationHeader, @RequestBody @Valid BookMarkCreateRequest bookMarkCreateRequest) {
        User user = jwtService.findUserByToken(authorizationHeader)
                .orElseThrow(() -> new UserNotFoundException("createBookMark"));
        bookMarkService.createBookMark(user, bookMarkCreateRequest);
        return "TicketId: " + bookMarkCreateRequest.getTicketId() + " - 즐겨찾기 추가 완료";
    }

    @DeleteMapping("/delete/{bookMarkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteBookMark(@RequestHeader(value = HEADER_AUTHORIZATION) String authorizationHeader, @PathVariable Long bookMarkId) {
        User user = jwtService.findUserByToken(authorizationHeader)
                .orElseThrow(() -> new UserNotFoundException("deleteBookMark"));
        bookMarkService.deleteBookMark(user, bookMarkId);
        return "BookMarkId: " + bookMarkId + " - 즐겨찾기 삭제 완료";
    }
}
