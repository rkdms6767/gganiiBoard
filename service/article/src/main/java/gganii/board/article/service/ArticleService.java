package gganii.board.article.service;

import gganii.board.article.PageLimitCalculator;
import gganii.board.article.service.request.ArticleCreateRequest;
import gganii.board.article.service.request.ArticleUpdateRequest;
import gganii.board.article.service.response.ArticlePageResponse;
import gganii.board.article.service.response.ArticleResponse;
import gganii.board.article.entity.Article;
import gganii.board.article.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(
                Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(), request.getWriterId())
        );
        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.update(request.getTitle(), request.getContent());
        articleRepository.save(article);
        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse delete(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        articleRepository.delete(article);
        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long articleId) {
        return ArticleResponse.from(articleRepository.findById(articleId).orElseThrow());
    }

    //article 리스트 로직 작성 (해당 board_id에 대한 페이지네이션)
    public ArticlePageResponse readAll(Long board_id, Long page, Long pageSize) {
        return ArticlePageResponse.of(
                articleRepository.findAll(board_id, (page-1) * pageSize, pageSize).stream()
                        .map(ArticleResponse::from)
                        .toList(),
                articleRepository.count(board_id, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L)
                )
        );
    }

    public List<ArticleResponse> findAllInfiniteScroll (Long board_id, Long pageSize, Long lastArticleId) {
        List<Article> articles = lastArticleId == null ?
                articleRepository.findAllInfiniteScroll(board_id, pageSize):
                articleRepository.findAllInfiniteScroll(board_id, pageSize, lastArticleId);

        return articles.stream().map(ArticleResponse::from).toList();
    }

}
