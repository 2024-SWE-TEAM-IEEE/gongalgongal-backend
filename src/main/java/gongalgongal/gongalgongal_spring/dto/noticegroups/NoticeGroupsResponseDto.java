package gongalgongal.gongalgongal_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeGroupsResponseDto extends ResponseTemplate<NoticeGroupsResponseDto.GroupListData> {

    @Builder
    public NoticeGroupsResponseDto(Status status, GroupListData data) {
        super(status, data);
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class GroupListData {
        private List<Group> groups;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Group {
        private Long id;
        private String name;
        private Long adminId;
        private String siteUrl;
        private String description;
        private String shareUrl;
        private List<Member> members;
        private boolean participant; // isParticipant -> participant로 변경
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Member {
        private Long id;
        private String name;
    }
}
