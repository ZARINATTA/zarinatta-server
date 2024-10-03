package com.zarinatta.zarinattaserver.bookmark.repository;

import com.zarinatta.zarinattaserver.bookmark.dto.request.MyBookMarkRequest;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.User;
import org.springframework.data.domain.Page;

public interface BookMarkRepositoryCustom {
    Page<BookMark> findMyBookMarkByRequest(User user, MyBookMarkRequest myBookMarkRequest);
}
