package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.chatrooms.ChatMessageRequestDto;
import gongalgongal.gongalgongal_spring.dto.chatrooms.ChatMessageResponseDto;
import gongalgongal.gongalgongal_spring.model.ChatMessage;
import gongalgongal.gongalgongal_spring.model.Chatroom;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.repository.ChatMessageRepository;
import gongalgongal.gongalgongal_spring.repository.ChatroomRepository;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ChatMessageResponseDto createChatMessage(Long noticeId, String email, ChatMessageRequestDto requestDto) {
        Chatroom chatroom = chatroomRepository.findByNoticeId(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("Chatroom not found"));

        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 메시지 생성 및 저장
        ChatMessage chatMessage = new ChatMessage(chatroom, user, requestDto.getContent(), LocalDateTime.now());
        ChatMessage savedMessage = chatMessageRepository.saveAndFlush(chatMessage);

        // 영속성 컨텍스트 초기화
//        entityManager.clear();

        // 응답 생성
        return new ChatMessageResponseDto(
                savedMessage.getId(),
                new ChatMessageResponseDto.AuthorDto(user.getId(), user.getName()),
                savedMessage.getContent(),
                savedMessage.getCreatedAt()
        );
    }
}
