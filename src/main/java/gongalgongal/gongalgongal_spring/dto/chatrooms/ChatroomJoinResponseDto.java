package gongalgongal.gongalgongal_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChatroomJoinResponseDto extends ResponseTemplate<ChatroomJoinResponseDto.ChatroomData> {

    public ChatroomJoinResponseDto(Status status, ChatroomData data) {
        super(status, data);
    }

    @Data
    @AllArgsConstructor
    public static class ChatroomData {
        private List<Chat> chats;
        private List<Member> members;
    }

    @Data
    @AllArgsConstructor
    public static class Chat {
        private Long id;
        private Author author;
        private String content;
        private LocalDateTime createdAt;
    }

    @Data
    @AllArgsConstructor
    public static class Author {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class Member {
        private Long id;
        private String name;
    }
}
