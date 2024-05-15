package com.abc.aftersale.utils;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/15 13:18
 * @注释
 */
@Component
public class MichatIdGenerator {
    private Set<String> michatIds;

    public MichatIdGenerator() {
        michatIds = new HashSet<>();
    }

    public String generatorMichatId(List<String> michatIds) {
        this.michatIds = new HashSet<>(michatIds);
        Random random = new Random();
        String michatId;
        do {
            int randomNumber = random.nextInt(900000000) + 100000000;
            michatId = String.valueOf(randomNumber);
        } while (michatIds.contains(michatId));

        return michatId;
    }
}
