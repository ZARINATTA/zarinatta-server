package com.zarinatta.zarinattaserver.ticket.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.zarinatta.zarinattaserver.entity.QTicket;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.ticket.controller.dto.request.TicketSearchRequest;

import java.util.List;

import static com.zarinatta.zarinattaserver.entity.QTicket.ticket;


@Repository
@RequiredArgsConstructor
public class TicketRepositoryCustomImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QTicket qTicket = ticket;

    @Override
    public Page<Ticket> findTicketBySearchDTO(TicketSearchRequest ticketSearchRequest, Pageable pageable) {
        List<Ticket> tickets = queryFactory
                .selectFrom(ticket)
                .where(ticket.departStation.eq(ticketSearchRequest.getDepartStation()),
                        ticket.arriveStation.eq(ticketSearchRequest.getArriveStation()),
                        ticket.departDate.eq(ticketSearchRequest.getDepartDate()),
                        ticket.departTime.goe(ticketSearchRequest.getDepartTime()),
                        ticketTypeEq(ticketSearchRequest.getTrainType()))
                .orderBy(ticket.departTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(tickets, pageable, countAll(ticketSearchRequest));
    }

    @Override
    public Long countAll(TicketSearchRequest ticketSearchRequest) {
        Long count = queryFactory
                .select(ticket.count())
                .from(ticket)
                .where(ticket.departStation.eq(ticketSearchRequest.getDepartStation()),
                        ticket.arriveStation.eq(ticketSearchRequest.getArriveStation()),
                        ticket.departDate.eq(ticketSearchRequest.getDepartDate()),
                        ticket.departTime.goe(ticketSearchRequest.getDepartTime()),
                        ticketTypeEq(ticketSearchRequest.getTrainType()))
                .fetchOne();
        return count;
    }

    private BooleanExpression ticketTypeEq(String trainType) {
        return (trainType != null) ? ticket.ticketType.contains(trainType) : null;
    }
}
