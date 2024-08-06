package com.rafi.esb.infra.routes.config;

public interface RouteProperties {
    //routes
    String ENABLE_TIMER_TO_VENDOR_REQUEST_BASIC_AUTH_SERVICE = "esb.infra.route.request.enable.timerToVendorBasicAuthService";
    String ENABLE_TIMER_TO_VENDOR_REQUEST_AUTH_SERVICE = "esb.infra.route.request.enable.timerToVendorAuthService";
    String ENABLE_TIMER_TO_VENDOR_RESPONSE_AUTH_SERVICE = "esb.infra.route.response.enable.timerToVendorAuthService";
    String ENABLE_ROUTE_REQUEST_P2G_TO_VENDOR = "esb.infra.route.request.enable.fromP2gToVendor";
    String ENABLE_ROUTE_REQUEST_P2G_AMQ_TO_VENDOR_AMQ = "esb.infra.route.request.enable.fromP2gAMQToVendorAMQ";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_TO_P2G = "esb.infra.route.response.enable.fromVendorToP2g";
    String ENABLE_ROUTE_SECOND_RESPONSE_FROM_VENDOR_TO_P2G = "esb.infra.route.second.response.enable.fromVendorToP2g";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_P2G_AMQ = "esb.infra.route.response.enable.fromVendorAMQToP2gAMQ";
    String ENABLE_ROUTE_REQUEST_FROM_P2G_AMQ_TO_VENDOR_IBMMQ = "esb.infra.route.request.enable.fromAmqToIbmMq";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_IBMMQ_TO_P2G_AMQ = "esb.infra.route.response.enable.fromIbmMqToAmq";
    String ENABLE_ROUTE_REQUEST_FROM_SFTP_PW_TO_VENDOR_AMQ = "esb.infra.route.request.enable.SftpPwToAmq";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_SFTP_PW = "esb.infra.route.response.enable.AmqToSftpPw";
    String ENABLE_ROUTE_REQUEST_FROM_SFTP_PK_TO_VENDOR_AMQ = "esb.infra.route.request.enable.SftpPkToAmq";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_SFTP_PK = "esb.infra.route.response.enable.AmqToSftpPk";

    String VENDOR_NAME = "esb.infra.vendor.name";
    String RESPONSE_SYNC = "esb.infra.response.sync";
    String RESPONSE_AUTHENTICATED_PATHS = "quarkus.http.auth.permission.authenticated.paths";
    //rest
    String REST_PERIOD = "esb.infra.?.rest.period";
    String REST_AUTH_URL = "esb.infra.?.rest.auth_url";
    String REST_GRANT_TYPE = "esb.infra.?.rest.grant_type";
    String REST_SCOPE = "esb.infra.?.rest.scope";
    String REST_USERNAME = "esb.infra.?.rest.username";
    String REST_PASSWORD = "esb.infra.?.rest.password";
    String REST_CLIENT_ID = "esb.infra.?.rest.client_id";
    String REST_CLIENT_SECRET = "esb.infra.?.rest.client_secret";
    String REQUEST_REST_METHOD = "esb.infra.rest.request.method";
    String REQUEST_REST_HEADERS = "esb.infra.request.rest.headers"; // key1=value1,key2=value2
    String REQUEST_REST_URL = "esb.infra.rest.request.url";
    String RESPONSE_REST_URL = "esb.infra.response.rest.url";
    //amq
    String P2G_AMQ_CONNECTION_FACTORY = "esb.infra.p2g.amq.connectionFactory";
    String VENDOR_AMQ_CONNECTION_FACTORY = "esb.infra.vendor.amq.connectionFactory";
    String REQUEST_P2G_AMQ_QUEUE_NAME = "esb.infra.p2g.amq.request.queue.name";
    String REQUEST_VENDOR_AMQ_QUEUE_NAME = "esb.infra.vendor.amq.request.queue.name";
    String RESPONSE_P2G_AMQ_QUEUE_NAME = "esb.infra.p2g.amq.response.queue.name";
    String RESPONSE_VENDOR_AMQ_QUEUE_NAME = "esb.infra.vendor.amq.response.queue.name";
    String SECOND_RESPONSE_P2G_AMQ_QUEUE_NAME = "esb.infra.p2g.amq.second.response.queue.name";
    //ibmmq
    String VENDOR_IBM_CONNECTION_FACTORY = "esb.infra.vendor.ibmmq.connectionFactory";
    String RESPONSE_VENDOR_IBMMQ_QUEUE_NAME = "esb.infra.vendor.ibmmq.response.queue.name";
    String REQUEST_VENDOR_IBMMQ_QUEUE_NAME = "esb.infra.vendor.ibmmq.request.queue.name";
    //sftp
    String SFTP_ADDRESS = "esb.infra.p2g.sftp.address";
    String SFTP_TO_FOLDER = "esb.infra.p2g.sftp.toFolder";
    String SFTP_USERNAME = "esb.infra.p2g.sftp.username";
    String SFTP_PASSWORD = "esb.infra.p2g.sftp.password";
    String SFTP_PRIVATE_KEY_FILE = "esb.infra.p2g.sftp.privateKeyFile";
    String SFTP_FROM_FOLDER = "esb.infra.p2g.sftp.fromFolder";
    String SFTP_ARCHIVE_FOLDER = "esb.infra.p2g.sftp.archiveFolder";
    String SFTP_SPLIT_ALGO = "esb.infra.p2g.sftp.splitAlgo";
    String SFTP_SPLIT_TAG = "esb.infra.p2g.sftp.splitTag";
    String SFTP_UPDATE_TAGS = "esb.infra.p2g.sftp.updateTags";

    default String resolveProperty(String property) {
        return "{{%s}}".formatted(property);
    }
    default String resolveProperty(String property,String replacement) {
        return "{{%s}}".formatted(property.replace("?", replacement.toLowerCase()));
    }
}
