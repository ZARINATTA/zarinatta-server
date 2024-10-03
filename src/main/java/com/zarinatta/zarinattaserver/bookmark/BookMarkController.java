package com.zarinatta.zarinattaserver.bookmark;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.exception.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
public class BookMarkController {

    private final BookMarkRepository bookMarkRepository;
    private final JwtService jwtService;

    @GetMapping("/search")
    public List<Long> searchBookMark(HttpServletRequest request, @RequestParam("ticketIds") List<Long> ticketIds) {
        String accessToken = (String) request.getAttribute("accessToken");
        User user = jwtService.findUserByToken(accessToken)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        List<BookMark> bookMarks = bookMarkRepository.findAllByTicketIdInAndUserId(ticketIds, user);
        List<Long> response = bookMarks.stream().map(bookMark -> bookMark.getId()).toList();
        return response;
    }
}
