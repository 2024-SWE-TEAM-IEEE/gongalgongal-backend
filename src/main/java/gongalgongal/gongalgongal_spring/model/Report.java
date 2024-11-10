package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import java.time.LocalDateTime;
import gongalgongal.gongalgongal_spring.model.enums.ReportStatus;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private Long messageId;

    private Long reportingUserId;

    private String reason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    // 기본 생성자
    public Report() {}

    // 생성자
    public Report(Long messageId, Long reportingUserId, String reason, ReportStatus status) {
        this.messageId = messageId;
        this.reportingUserId = reportingUserId;
        this.reason = reason;
        this.status = status;
    }
}