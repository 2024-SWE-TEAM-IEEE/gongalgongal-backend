package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private Long messageId;

    private Long reportingUserId;

    private String reason;

    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    // 기본 생성자
    public Report() {}

    // 생성자
    public Report(Long messageId, Long reportingUserId, String reason, Timestamp createdAt, ReportStatus status) {
        this.messageId = messageId;
        this.reportingUserId = reportingUserId;
        this.reason = reason;
        this.createdAt = createdAt;
        this.status = status;
    }
}

// ENUM 타입 정의
public enum ReportStatus {
    Received, InProgress, Completed
}
