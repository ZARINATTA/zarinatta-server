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
import com.zarinatta.zarinattaserver.ticket.dto.request.TicketSearchRequest;

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

    @Override
    public void insertMockTicketData() {
        String nativeQuery = "INSERT INTO ticket (ticket_id, ticket_type, depart_date, depart_time, depart_station, arrive_time, arrive_station, price)\n" +
                "VALUES \n" +
                "    (REPLACE(CONCAT(CURDATE(), '080000'), '-', ''), 'KTX-이음 101', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '080000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '100000'), '-', ''), '대전', '15000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '090000'), '-', ''), 'SRT 202', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '090000'), '-', ''), '부산', REPLACE(CONCAT(CURDATE(), '113000'), '-', ''), '서울', '50000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '103000'), '-', ''), 'KTX 303', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '103000'), '-', ''), '대구', REPLACE(CONCAT(CURDATE(), '120000'), '-', ''), '광주', '35000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '110000'), '-', ''), 'ITX-새마을 404', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '110000'), '-', ''), '전주', REPLACE(CONCAT(CURDATE(), '124500'), '-', ''), '서울', '28000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '130000'), '-', ''), 'KTX 505', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '130000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '145500'), '-', ''), '부산', '55000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '140000'), '-', ''), 'SRT 606', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '140000'), '-', ''), '수서', REPLACE(CONCAT(CURDATE(), '160000'), '-', ''), '목포', '48000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '150000'), '-', ''), '무궁화호 707', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '150000'), '-', ''), '광주', REPLACE(CONCAT(CURDATE(), '180000'), '-', ''), '부산', '32000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '160000'), '-', ''), '누리로 808', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '160000'), '-', ''), '대전', REPLACE(CONCAT(CURDATE(), '175500'), '-', ''), '서울', '17000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '170000'), '-', ''), 'KTX-이음 909', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '170000'), '-', ''), '평창', REPLACE(CONCAT(CURDATE(), '172500'), '-', ''), '강릉', '9000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '180000'), '-', ''), 'SRT 1010', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '180000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '195500'), '-', ''), '부산', '55000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '190000'), '-', ''), 'ITX-청춘 1111', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '190000'), '-', ''), '용산', REPLACE(CONCAT(CURDATE(), '211500'), '-', ''), '춘천', '12000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '200000'), '-', ''), 'KTX 1212', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '200000'), '-', ''), '동대구', REPLACE(CONCAT(CURDATE(), '221500'), '-', ''), '서울', '45000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '210000'), '-', ''), '무궁화호 1313', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '210000'), '-', ''), '부산', REPLACE(CONCAT(CURDATE(), '235500'), '-', ''), '광주', '38000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '220000'), '-', ''), 'SRT 1414', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '220000'), '-', ''), '서울', REPLACE(CONCAT(CURDATE(), '235500'), '-', ''), '전주', '25000원'),\n" +
                "    (REPLACE(CONCAT(CURDATE(), '230000'), '-', ''), 'KTX-이음 1515', REPLACE(CURDATE(), '-', ''), REPLACE(CONCAT(CURDATE(), '230000'), '-', ''), '강릉', REPLACE(CONCAT(CURDATE(), '234500'), '-', ''), '서울', '30000원');\n";
    }

    private BooleanExpression ticketTypeEq(String trainType) {
        return (trainType != null) ? ticket.ticketType.contains(trainType) : null;
    }
}
