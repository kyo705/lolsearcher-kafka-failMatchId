package com.lolsearcher.persistance.failmatchids.constant;


public class FailMatchIdConsumerConstants {

    public static final String BOOTSTRAP_SERVER = "localhost:9092";

    public static final String LISTENER_CONTAINER_FACTORY_NAME = "failMatchIdsContainerFactory";

    public static final String GROUP_ID = "filtered_fail_match_id_group";

    public static final String LISTENER_ID = "filtered_fail_match_id_listener";

    public static final String AUTO_OFFSET_RESET = "earliest";

    public static final String ISOLATION_LEVEL = "read_committed";

    public static final int POLL_RECORDS_COUNT = 100; /* riot api 요청 제한 횟수가 2분당 최대 100회 */

    public static final int HEARTBEAT_MS = 2000;

    public static final int SESSION_TIME_OUT_MS = 6000;

    public static final int POLL_MS = 180000; /* 3min */

    public static final boolean AUTO_OFFSET_COMMIT = false;


}
