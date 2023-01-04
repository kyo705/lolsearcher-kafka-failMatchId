package com.lolsearcher.persistance.failmatchids.constant;


import org.apache.kafka.common.record.CompressionType;

public class SuccessMatchProducerConstants {

    public static final  String BOOTSTRAP_SERVER = "localhost:9092";

    public static final  String ACK_MODE = "all";

    public static final  String COMPRESSION_TYPE = CompressionType.ZSTD.name;

    public static final boolean IDEMPOTENCE = true;
}
