package gongalgongal.gongalgongal_spring.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeStarDeleteResponseDto extends ResponseTemplate<Object> {

    public NoticeStarDeleteResponseDto(Status status) {
        super(status, null);
    }
}