package com.lolsearcher.persistance.failmatchids.service.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;

public interface producerRecordService<K, V> {

    ProducerRecord<K, V> createProducerRecord(V value);

    ProducerRecord<K, V> createProducerRecord(K key, V value);

    ProducerRecord<K, V> createProducerRecord(int partition, K key, V value);

}
