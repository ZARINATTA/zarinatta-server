package com.zarinatta.zarinattaserver.bookmark;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.exception.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zarinatta.zarinattaserver.enums.Values.HEADER_AUTHORIZATION;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
public class BookMarkController {

    private final BookMarkRepository bookMarkRepository;
    private final JwtService jwtService;

    @GetMapping("/search")
    public List<Long> searchBookMark(@RequestHeader(value = HEADER_AUTHORIZATION) String authorizationHeader, @RequestParam("ticketIds") List<Long> ticketIds) {
        User user = jwtService.findUserByToken(authorizationHeader)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        List<BookMark> bookMarks = bookMarkRepository.findAllByTicketIdInAndUserId(ticketIds, user);
        List<Long> response = bookMarks.stream().map(bookMark -> bookMark.getId()).toList();
        return response;
    }
}
