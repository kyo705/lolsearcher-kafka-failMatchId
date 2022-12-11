package com.lolsearcher.persistance.failmatchids.service.kafka.producer;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuccessMatchProducerService {
    public void send(List<Match> successMatches) {
    }
}
