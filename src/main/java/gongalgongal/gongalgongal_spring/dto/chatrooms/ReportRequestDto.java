package gongalgongal.gongalgongal_spring.dto.chatrooms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportRequestDto {
    private String reason; // 신고 사유
}