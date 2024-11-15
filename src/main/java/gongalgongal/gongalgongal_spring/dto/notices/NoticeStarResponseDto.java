package gongalgongal.gongalgongal_spring.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeStarResponseDto extends ResponseTemplate<Object> {

    public NoticeStarResponseDto(Status status) {
        super(status, null);
    }
}