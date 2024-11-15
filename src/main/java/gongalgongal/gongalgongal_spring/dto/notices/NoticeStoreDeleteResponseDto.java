package gongalgongal.gongalgongal_spring.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeStoreDeleteResponseDto extends ResponseTemplate<Object> {

    public NoticeStoreDeleteResponseDto(Status status) {
        super(status, null);
    }
}