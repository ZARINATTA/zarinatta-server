package com.zarinatta.zarinattaserver.bookmark.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zarinatta.zarinattaserver.bookmark.dto.request.MyBookMarkRequest;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.QBookMark;
import com.zarinatta.zarinattaserver.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookMarkRepositoryCustomImpl implements BookMarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QBookMark qBookMark = QBookMark.bookMark;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    @Override
    public Page<BookMark> findMyBookMarkByRequest(User user, MyBookMarkRequest myBookMarkRequest) {
        List<BookMark> bookMarks = queryFactory
                .selectFrom(qBookMark)
                .where(qBookMark.user.eq(user),
                        isExpire(myBookMarkRequest.getExpire()))
                .join(qBookMark.ticket).fetchJoin()
                .offset(myBookMarkRequest.getPage() * myBookMarkRequest.getSize())
                .limit(myBookMarkRequest.getSize())
                .fetch();
        Pageable pageable = PageRequest.of(myBookMarkRequest.getPage(), myBookMarkRequest.getSize());
        return new PageImpl<>(bookMarks, pageable, countAll(user, myBookMarkRequest));
    }

    private Long countAll(User user, MyBookMarkRequest myBookMarkRequest) {
        Long count = queryFactory
                .select(qBookMark.count())
                .from(qBookMark)
                .where(qBookMark.user.eq(user),
                        isExpire(myBookMarkRequest.getExpire()))
                .fetchOne();
        return count;
    }

    private BooleanExpression isExpire(Boolean expire) {
        return expire ? qBookMark.ticket.departTime.lt(LocalDateTime.now().format(formatter)) : qBookMark.ticket.departTime.goe(LocalDateTime.now().format(formatter));
    }
}
