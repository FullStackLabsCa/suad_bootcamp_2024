package io.reactivestax.repository.hibernate.entity;

import io.reactivestax.types.enums.Direction;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "journal_entries")
@Data
@Builder
public class JournalEntries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trade_id")
    private String tradeId;

    @Column(name = "trade_date")
    private String tradeDate;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name="cusip")
    private String cusip;

    @Column(name = "direction", nullable = false)
    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name ="price")
    private Double price;

    @Column(name = "posted_date")
    @CreationTimestamp
    private Date postedDate;
}
