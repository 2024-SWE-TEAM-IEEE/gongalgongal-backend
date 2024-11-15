package gongalgongal.gongalgongal_spring.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeDeleteResponseDto extends ResponseTemplate<Object> {

    public NoticeDeleteResponseDto(Status status) {
        super(status, null);
    }
}