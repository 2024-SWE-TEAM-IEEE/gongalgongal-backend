package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.ChatroomJoinResponseDto;
import gongalgongal.gongalgongal_spring.dto.ChatroomListResponseDto;

import gongalgongal.gongalgongal_spring.model.Chatroom;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.ChatMessage;
import gongalgongal.gongalgongal_spring.model.Notice;

import gongalgongal.gongalgongal_spring.repository.ChatroomRepository;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import gongalgongal.gongalgongal_spring.repository.ChatMessageRepository;
import gongalgongal.gongalgongal_spring.repository.NoticeRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NoticeRepository noticeRepository;

    @Autowired
    public ChatroomService(ChatroomRepository chatroomRepository, UserRepository userRepository, ChatMessageRepository chatMessageRepository, NoticeRepository noticeRepository) {
        this.chatroomRepository = chatroomRepository;
        this.userRepository = userRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.noticeRepository = noticeRepository;
    }

    public ChatroomListResponseDto getAllChatrooms(Authentication authentication) {
        // 사용자 이메일 조회
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 사용자의 모든 채팅방 조회
        List<Chatroom> chatrooms = new ArrayList<>(user.getChatrooms());

        // 채팅방 목록 데이터 생성
        List<ChatroomListResponseDto.Chatroom> chatroomList = chatrooms.stream().map(chatroom -> {
            // Notice 정보 조회
            Notice notice = noticeRepository.findById(chatroom.getNoticeId())
                    .orElseThrow(() -> new IllegalArgumentException("Notice not found for chatroom ID: " + chatroom.getChatId()));

            // 카테고리 데이터 변환
            List<ChatroomListResponseDto.Category> categories = notice.getCategories().stream()
                    .map(category -> new ChatroomListResponseDto.Category(category.getCategoryId(), category.getCategoryName()))
                    .collect(Collectors.toList());

            // 멤버 데이터 변환
            List<ChatroomListResponseDto.Member> members = chatroom.getUsers().stream()
                    .map(userMember -> new ChatroomListResponseDto.Member(
                            userMember.getId(),
                            userMember.getName(),
                            userMember.getEmail() // 이메일 추가
                    ))
                    .collect(Collectors.toList());

            // 채팅방 데이터 반환
            return new ChatroomListResponseDto.Chatroom(
                    chatroom.getChatId(),
                    chatroom.getNoticeId(),
                    categories,
                    notice.getTitle(), // Notice의 제목 가져오기
                    members
            );
        }).collect(Collectors.toList());

        // 응답 데이터 생성
        ChatroomListResponseDto.ChatroomListData chatroomListData = new ChatroomListResponseDto.ChatroomListData(chatroomList);

        return new ChatroomListResponseDto(
                new ChatroomListResponseDto.Status("success", "Chatrooms retrieved successfully"),
                chatroomListData
        );
    }


    public ChatroomJoinResponseDto joinChatroom(Long noticeId, Authentication authentication) {
        // 사용자 이메일 조회
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 채팅방 조회 또는 생성
        Chatroom chatroom = chatroomRepository.findByNoticeId(noticeId)
                .orElseGet(() -> {
                    Chatroom newChatroom = new Chatroom(noticeId);
                    try {
                        chatroomRepository.save(newChatroom);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create a new chatroom", e);
                    }
                    return newChatroom;
                });

        // 채팅방 유저 목록 초기화 체크
        if (chatroom.getUsers() == null) {
            chatroom.setUsers(new HashSet<>());
        }

        // 사용자가 채팅방에 이미 참여 중인지 확인
        if (!chatroom.getUsers().contains(user)) {
            chatroom.getUsers().add(user);
            user.getChatrooms().add(chatroom); // User의 chatrooms에도 추가
            try {
                chatroomRepository.save(chatroom);
                userRepository.save(user); // User도 저장
            } catch (Exception e) {
                throw new RuntimeException("Failed to add user to chatroom", e);
            }
        }
        else {
            throw new RuntimeException("User is already a member of the chatroom");
        }

        // 채팅 메시지 조회
        List<ChatroomJoinResponseDto.Chat> chats;
        try {
            chats = chatMessageRepository.findByChatroom(chatroom).stream()
                    .map(msg -> new ChatroomJoinResponseDto.Chat(
                            msg.getId(),
                            new ChatroomJoinResponseDto.Author(msg.getUser().getId(), msg.getUser().getName()),
                            msg.getContent(),
                            msg.getCreatedAt()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch chat messages for chatroom", e);
        }

        // 채팅방 멤버 조회
        List<ChatroomJoinResponseDto.Member> members = chatroom.getUsers().stream()
                .map(member -> new ChatroomJoinResponseDto.Member(member.getId(), member.getName()))
                .collect(Collectors.toList());

        // ChatroomData 생성
        ChatroomJoinResponseDto.ChatroomData chatroomData = new ChatroomJoinResponseDto.ChatroomData(chats, members);

        return new ChatroomJoinResponseDto(
                new ChatroomJoinResponseDto.Status("success", "Chatroom joined successfully"),
                chatroomData
        );

    }
}
