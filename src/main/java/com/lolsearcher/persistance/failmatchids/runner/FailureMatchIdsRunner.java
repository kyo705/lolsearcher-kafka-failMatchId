package com.lolsearcher.persistance.failmatchids.runner;

import com.lolsearcher.persistance.failmatchids.model.dto.SuccessMatchesAndFailMatchIds;
import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import com.lolsearcher.persistance.failmatchids.service.FailureMatchIdService;
import com.lolsearcher.persistance.failmatchids.service.SuccessMatchService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FailureMatchIdsRunner implements CommandLineRunner {
    private final SuccessMatchService successMatchService;
    private final FailureMatchIdService failureMatchIdService;

    @Override
    public void run(String... args) throws Exception {
        while(true){
            List<String> beforeFailMatchIds = failureMatchIdService.getFailMatchIds();
            SuccessMatchesAndFailMatchIds successMatchesAndFailMatchIds = failureMatchIdService.requestMatches(beforeFailMatchIds);

            List<Match> successMatches = successMatchesAndFailMatchIds.getSuccessMatches();
            List<String> failMatchIds = successMatchesAndFailMatchIds.getFailMatchIds();

            successMatchService.saveSuccessMatches(successMatches);
            failureMatchIdService.saveFailMatchIds(failMatchIds);
        }
    }
}
