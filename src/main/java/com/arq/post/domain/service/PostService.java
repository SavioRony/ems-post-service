package com.arq.post.domain.service;

import com.arq.post.api.model.PostInput;
import com.arq.post.api.model.PostOutput;
import com.arq.post.api.model.PostSummaryOutput;
import com.arq.post.api.model.PostTextProcessorResult;
import com.arq.post.common.PostMapper;
import com.arq.post.domain.model.Post;
import com.arq.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.arq.post.rabbitmq.RabbitMQConfig.EXCHANGE_TEXT_PROCESSOR;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final RabbitTemplate rabbitTemplate;

    public void createPost(PostInput postInput) {
        log.info("📝 Criando novo post com título='{}'", postInput.getTitle());
        Post saved = postRepository.save(PostMapper.toEntity(postInput));
        PostOutput output = PostMapper.toOutput(saved);

        log.info("✅ Post salvo com ID='{}', enviando para processamento de texto", output.getId());
        sendToQueue(output);
    }

    public void savedResultTextProcessor(PostTextProcessorResult postTextProcessorResult) {
        log.info("📥 Recebido resultado de processamento de texto para postId='{}'", postTextProcessorResult.getPostId());

        Optional<Post> savedOptional = postRepository.findById(postTextProcessorResult.getPostId());
        if (savedOptional.isPresent()) {
            Post saved = savedOptional.get();
            saved.setWordCount(postTextProcessorResult.getWordCount());
            saved.setCalculatedValue(postTextProcessorResult.getCalculatedValue());
            postRepository.save(saved);

            log.info("💾 Atualizado postId='{}' com wordCount={} e calculatedValue={}",
                    saved.getId(), saved.getWordCount(), saved.getCalculatedValue());
        } else {
            log.warn("⚠️ Post com ID='{}' não encontrado ao tentar aplicar resultado de processamento",
                    postTextProcessorResult.getPostId());
        }
    }

    public Optional<PostOutput> findById(String id) {
        log.info("🔍 Buscando post por ID='{}'", id);
        return postRepository.findById(id)
                .map(post -> {
                    log.info("✅ Post encontrado: ID='{}'", id);
                    return PostMapper.toOutputComplete(post);
                });
    }

    public Page<PostSummaryOutput> findAll(PageRequest pageRequest) {
        log.info("📄 Listando posts página={}, tamanho={}", pageRequest.getPageNumber(), pageRequest.getPageSize());
        return postRepository.findAll(pageRequest)
                .map(PostMapper::toOutputSumary);
    }

    public void sendToQueue(PostOutput post) {
        String routingKey = "";
        Object payload = PostMapper.toTextProcessorInput(post);

        log.debug("📤 Enviando postId='{}' para fila de processamento de texto", post.getId());
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_TEXT_PROCESSOR, routingKey, payload);
            log.info("✅ Mensagem enviada para exchange='{}' com routingKey='{}'", EXCHANGE_TEXT_PROCESSOR, routingKey);
        } catch (Exception e) {
            log.error("❌ Erro ao enviar postId='{}' para fila: {}", post.getId(), e.getMessage(), e);
            throw e;
        }
    }
}
