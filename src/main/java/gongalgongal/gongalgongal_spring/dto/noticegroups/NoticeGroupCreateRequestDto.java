/*
API: 공지 그룹 생성
Request:
    {
        "group_name": "새 공지 그룹",
        "description": "공지 그룹에 대한 설명",
        "group_category": [
          1,  // 장학
          2   // 교외활동
        ],
        "crawl_site_url": "https://www.dongguk.edu/article/JANGHAKNOTICE/list",
    }
*/

package gongalgongal.gongalgongal_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class NoticeGroupCreateRequestDto {
    private String groupName;
    private String description;
    private List<Integer> groupCategory;
    private String crawlSiteUrl;
}
