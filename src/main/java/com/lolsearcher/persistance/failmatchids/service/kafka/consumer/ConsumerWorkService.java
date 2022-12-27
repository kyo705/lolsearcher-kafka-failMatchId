package com.lolsearcher.persistance.failmatchids.service.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@Service
public class ConsumerWorkService {

    private final KafkaListenerEndpointRegistry registry;

    public void pause(String id){
        requireNonNull(registry.getListenerContainer(id)).pause();
    }

    public void resume(String id){
        requireNonNull(registry.getListenerContainer(id)).resume();
    }

    public void destroy(String id){
        requireNonNull(registry.getListenerContainer(id)).destroy();
        registry.unregisterListenerContainer(id);
    }
}
