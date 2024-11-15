package gongalgongal.gongalgongal_spring.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeStoreResponseDto extends ResponseTemplate<Object> {

    public NoticeStoreResponseDto(Status status) {
        super(status, null);
    }
}