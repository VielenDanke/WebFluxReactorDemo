package dankekz.catalizator.service.impl;

import dankekz.catalizator.domain.Message;
import dankekz.catalizator.repository.MessageRepository;
import dankekz.catalizator.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public Flux<Message> list() {
        return messageRepository.findAll();
    }

    public Mono<Message> addOne(Message message) {
        return messageRepository.save(message);
    }
}
