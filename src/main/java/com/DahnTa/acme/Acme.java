package com.DahnTa.acme;

import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.AccountBuilder;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.challenge.Challenge;
import org.shredzone.acme4j.challenge.Http01Challenge;
import org.shredzone.acme4j.util.KeyPairUtils;

import java.io.File;
import java.io.FileWriter;
import java.security.KeyPair;
import java.util.Optional;

public class Acme {

    public static void main(String[] args) throws Exception {
        String domain = "dahnta-server.duckdns.org";
        File challengeDir = new File("src/main/resources/static/.well-known/acme-challenge");
        challengeDir.mkdirs();

        KeyPair userKeyPair = KeyPairUtils.createKeyPair(2048);
        Session session = new Session("acme://letsencrypt.org/staging");

        Account account = new AccountBuilder()
            .addContact("mailto:kim0615157@gmail.com")
            .agreeToTermsOfService()
            .useKeyPair(userKeyPair)
            .create(session);

        Order order = account.newOrder().domains(domain).create();

        for (Authorization auth : order.getAuthorizations()) {
            Optional<Challenge> optChallenge = auth.findChallenge(Http01Challenge.TYPE);
            if (optChallenge.isPresent() && optChallenge.get() instanceof Http01Challenge) {
                Http01Challenge httpChallenge = (Http01Challenge) optChallenge.get();
                try {
                    String token = httpChallenge.getToken();
                    String content = httpChallenge.getAuthorization();

                    File challengeFile = new File(challengeDir, token);
                    try (FileWriter fw = new FileWriter(challengeFile)) {
                        fw.write(content);
                    }

                    httpChallenge.trigger();
                    auth.update();
                    System.out.println("HTTP-01 Challenge 파일 생성 완료: " + challengeFile.getAbsolutePath());
                    System.out.println("인증 상태: " + auth.getStatus());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("HTTP-01 Challenge를 찾을 수 없습니다. Optional 비어있음 또는 타입 불일치");
            }
        }
    }
}
