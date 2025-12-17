package gganii.board.article.service.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class ArticlePageResponse {
    // entity article list를 articleresponse 로
    //articleresponse를 pageResponse로
    private List<ArticleResponse> articles;
    private Long articleCount;

    public static ArticlePageResponse of(List<ArticleResponse> articleResponses, Long articleCount) {
        ArticlePageResponse response = new ArticlePageResponse();
        response.articles = articleResponses;
        response.articleCount = articleCount;
        return response;
    }


}
