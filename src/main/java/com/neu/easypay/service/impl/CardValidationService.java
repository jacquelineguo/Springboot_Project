package com.neu.easypay.service.impl;

import com.neu.easypay.model.CardValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CardValidationService {

    public CardValidationResponse checkCreditCardNumber(final String creditCardNumber) {
        final String url = "https://c3jkkrjnzlvl5lxof74vldwug40pxsqo.lambda-url.us-west-2.on.aws";

        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"card_number\":\"" + creditCardNumber + "\"}", httpHeaders);
        CardValidationResponse response = restTemplate.postForObject(url, request, CardValidationResponse.class);
        System.out.println(response);
        return response;
    }

    public CardValidationResponse checkDebitCardSufficientFund(
        final String debutCardNumber,
        final Double amount
    ) {
        final String url = "https://223didiouo3hh4krxhm4n4gv7y0pfzxk.lambda-url.us-west-2.on.aws/";

        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(
            "{"
                + "\"card_number\":\"" + debutCardNumber + "\","
                + "\"amt\":" + amount + ""
                + "}", httpHeaders);
        CardValidationResponse response = restTemplate
            .postForObject(url, request, CardValidationResponse.class);
        System.out.println(response);
        return response;
    }
}
