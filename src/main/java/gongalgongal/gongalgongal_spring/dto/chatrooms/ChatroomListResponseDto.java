package gongalgongal.gongalgongal_spring.dto;

import lombok.*;

import java.util.List;

// ResponseTemplate을 상속한 ChatroomListResponseDto 정의
@Data
@EqualsAndHashCode(callSuper = false)
public class ChatroomListResponseDto extends ResponseTemplate<ChatroomListResponseDto.ChatroomListData> {

    public ChatroomListResponseDto(Status status, ChatroomListData data) {
        super(status, data);
    }

    // ChatroomListResponseDto의 Data 클래스 정의
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatroomListData {
        private List<Chatroom> chatroom; // 채팅방 리스트
    }

    // Chatroom 내부 클래스 정의
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Chatroom {
        private Long id;               // chatroom id
        private Long noticeId;         // notice id
        private String title;          // 채팅방 제목
        private List<Member> members;  // 멤버 리스트
    }

    // Member 내부 클래스 정의
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Member {
        private Long id;     // 멤버 id
        private String name; // 멤버 이름
        private String email; // 멤버 이메일 (예시 추가)
    }
}
