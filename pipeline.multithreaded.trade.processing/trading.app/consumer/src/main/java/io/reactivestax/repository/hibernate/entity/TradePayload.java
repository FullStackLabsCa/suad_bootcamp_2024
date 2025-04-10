package io.reactivestax.repository.hibernate.entity;

import io.reactivestax.types.enums.LookUpStatusEnum;
import io.reactivestax.types.enums.PostedStatusEnum;
import io.reactivestax.types.enums.StatusReasonEnum;
import io.reactivestax.types.enums.ValidityStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "trade_payloads")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradePayload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trade_id")
    private String tradeId;

    @Column(name = "validity_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ValidityStatusEnum validityStatus = ValidityStatusEnum.INVALID;

    @Column(name = "status_reason", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusReasonEnum statusReason = StatusReasonEnum.FIELDS_MISSING;

    @Column(name = "lookup_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LookUpStatusEnum lookupStatus = LookUpStatusEnum.FAIL;

    @Column(name = "je_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostedStatusEnum jeStatus = PostedStatusEnum.NOT_POSTED;

    @Column(name = "payload")
    private String payload;

    @Column(name = "created_date_time")
    @CreationTimestamp
    private Date createdDateTime;
}
