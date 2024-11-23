package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.chatrooms.ReportRequestDto;
import gongalgongal.gongalgongal_spring.dto.chatrooms.ReportResponseDto;
import gongalgongal.gongalgongal_spring.model.ChatMessage;
import gongalgongal.gongalgongal_spring.model.Report;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.repository.ChatMessageRepository;
import gongalgongal.gongalgongal_spring.repository.ReportRepository;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ReportResponseDto createReport(Long chatId, Long reportingUserId, ReportRequestDto requestDto) {
        ChatMessage chatMessage = chatMessageRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("ChatMessage not found"));

        User reportingUser = userRepository.findById(reportingUserId)
                .orElseThrow(() -> new IllegalArgumentException("Reporting User not found"));

        Report report = new Report();
        report.setChatMessage(chatMessage);
        report.setReportingUser(reportingUser);
        report.setReason(requestDto.getReason());
        report.setCreatedAt(LocalDateTime.now());

        Report savedReport = reportRepository.save(report);

        return new ReportResponseDto(
                savedReport.getId(),
                chatMessage.getId(),
                chatMessage.getContent(),
                new ReportResponseDto.ReportingUserDto(reportingUser.getId(), reportingUser.getName()),
                savedReport.getCreatedAt()
        );
    }
}
