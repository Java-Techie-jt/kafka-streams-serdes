package com.javatechie.events;

public record Transaction(
        String transactionId,
        String userId,
        double amount,
        String timestamp
) {
}

//serializer //Deserializer
//serdes