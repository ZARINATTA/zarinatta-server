package com.zarinatta.zarinattaserver.bookmark.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
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
                .where(qBookMark.user.eq(user), qBookMark.isDeleted.isFalse(),
                        isExpire(myBookMarkRequest.expire()))
                .join(qBookMark.ticket).fetchJoin()
                .offset(myBookMarkRequest.page() * myBookMarkRequest.size())
                .limit(myBookMarkRequest.size())
                .fetch();
        Pageable pageable = PageRequest.of(myBookMarkRequest.page(), myBookMarkRequest.size());
        return new PageImpl<>(bookMarks, pageable, countAll(user, myBookMarkRequest));
    }

    private Long countAll(User user, MyBookMarkRequest myBookMarkRequest) {
        Long count = queryFactory
                .select(qBookMark.count())
                .from(qBookMark)
                .where(qBookMark.user.eq(user),
                        isExpire(myBookMarkRequest.expire()))
                .fetchOne();
        return count;
    }

    private BooleanExpression isExpire(Boolean expire) {
        StringExpression dateTime = Expressions.stringTemplate("CONCAT({0}, {1})",
                qBookMark.ticket.departDate,
                qBookMark.ticket.departTime);
        return expire ? dateTime.lt(LocalDateTime.now().format(formatter)) : dateTime.goe(LocalDateTime.now().format(formatter));
    }
}
