package com.zarinatta.zarinattaserver.ticket.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.zarinatta.zarinattaserver.entity.QTicket;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.ticket.dto.request.TicketSearchRequest;

import java.util.List;

import static com.zarinatta.zarinattaserver.entity.QTicket.ticket;


@Repository
@RequiredArgsConstructor
public class TicketRepositoryCustomImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    private QTicket qTicket = ticket;

    @Override
    public Page<Ticket> findTicketBySearchDTO(TicketSearchRequest ticketSearchRequest, Pageable pageable) {
        List<Ticket> tickets = queryFactory
                .selectFrom(ticket)
                .where(ticket.departStation.eq(ticketSearchRequest.departStation()),
                        ticket.arriveStation.eq(ticketSearchRequest.arriveStation()),
                        ticket.departDate.eq(ticketSearchRequest.departDate()),
                        departTimeEq(ticketSearchRequest.departTime()),
                        ticketTypeEq(ticketSearchRequest.trainType()))
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
                .where(ticket.departStation.eq(ticketSearchRequest.departStation()),
                        ticket.arriveStation.eq(ticketSearchRequest.arriveStation()),
                        ticket.departDate.eq(ticketSearchRequest.departDate()),
                        departTimeEq(ticketSearchRequest.departTime()),
                        ticketTypeEq(ticketSearchRequest.trainType()))
                .fetchOne();
        return count;
    }

    /**
     * Warning : 하루에 한번만 요청 보내야 함.
     */
    @Override
    public void insertMockTicketData() {
        String nativeQuery = "INSERT INTO ticket (ticket_id, ticket_type, depart_date, depart_time, depart_station, arrive_time, arrive_station, price)\n" +
                "VALUES \n" +
                "    (REPLACE(CONCAT(CURDATE(), '080000'), '-', ''), 'KTX-이음 101', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '080000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '100000'), '-', ''), '부산', '15000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '090000'), '-', ''), 'SRT 202', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '090000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '113000'), '-', ''), '부산', '50000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '103000'), '-', ''), 'KTX 303', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '103000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '120000'), '-', ''), '부산', '35000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '110000'), '-', ''), 'ITX-새마을 404', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '110000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '124500'), '-', ''), '부산', '28000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '130000'), '-', ''), 'KTX 505', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '130000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '145500'), '-', ''), '부산', '55000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '140000'), '-', ''), 'SRT 606', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '140000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '160000'), '-', ''), '부산', '48000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '150000'), '-', ''), '무궁화호 707', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '150000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '180000'), '-', ''), '부산', '32000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '160000'), '-', ''), '누리로 808', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '160000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '175500'), '-', ''), '부산', '17000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '170000'), '-', ''), 'KTX-이음 909', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '170000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '172500'), '-', ''), '부산', '9000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '180000'), '-', ''), 'SRT 1010', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '180000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '195500'), '-', ''), '부산', '55000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '190000'), '-', ''), 'ITX-청춘 1111', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '190000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '211500'), '-', ''), '부산', '12000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '200000'), '-', ''), 'KTX 1212', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '200000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '221500'), '-', ''), '부산', '45000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '210000'), '-', ''), '무궁화호 1313', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '210000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '235500'), '-', ''), '부산', '38000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '220000'), '-', ''), 'SRT 1414', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '220000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '235500'), '-', ''), '부산', '25000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '230000'), '-', ''), 'KTX-이음 1515', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '230000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '234500'), '-', ''), '부산', '30000원');\n";

        entityManager.createNativeQuery(nativeQuery).executeUpdate();
    }

    private BooleanExpression departTimeEq(String departTime) {
        return (departTime != null) ? ticket.departTime.goe(departTime) : null;
    }

    private BooleanExpression ticketTypeEq(String trainType) {
        return (trainType != null) ? ticket.ticketType.contains(trainType) : null;
    }
}
