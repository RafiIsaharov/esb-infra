package com.rafi.esb.infra.routes.config;

public interface RouteProperties {
    //routes
    String ENABLE_TIMER_TO_VENDOR_REQUEST_BASIC_AUTH_SERVICE = "finastra.camel.esb.route.request.enable.timerToVendorBasicAuthService";
    String ENABLE_TIMER_TO_VENDOR_REQUEST_AUTH_SERVICE = "finastra.camel.esb.route.request.enable.timerToVendorAuthService";
    String ENABLE_TIMER_TO_VENDOR_RESPONSE_AUTH_SERVICE = "finastra.camel.esb.route.response.enable.timerToVendorAuthService";
    String ENABLE_ROUTE_REQUEST_P2G_TO_VENDOR = "finastra.camel.esb.route.request.enable.fromP2gToVendor";
    String ENABLE_ROUTE_REQUEST_P2G_AMQ_TO_VENDOR_AMQ = "finastra.camel.esb.route.request.enable.fromP2gAMQToVendorAMQ";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_TO_P2G = "finastra.camel.esb.route.response.enable.fromVendorToP2g";
    String ENABLE_ROUTE_SECOND_RESPONSE_FROM_VENDOR_TO_P2G = "finastra.camel.esb.route.second.response.enable.fromVendorToP2g";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_P2G_AMQ = "finastra.camel.esb.route.response.enable.fromVendorAMQToP2gAMQ";
    String ENABLE_ROUTE_REQUEST_FROM_P2G_AMQ_TO_VENDOR_IBMMQ = "finastra.camel.esb.route.request.enable.fromAmqToIbmMq";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_IBMMQ_TO_P2G_AMQ = "finastra.camel.esb.route.response.enable.fromIbmMqToAmq";
    String ENABLE_ROUTE_REQUEST_FROM_SFTP_PW_TO_VENDOR_AMQ = "finastra.camel.esb.route.request.enable.SftpPwToAmq";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_SFTP_PW = "finastra.camel.esb.route.response.enable.AmqToSftpPw";
    String ENABLE_ROUTE_REQUEST_FROM_SFTP_PK_TO_VENDOR_AMQ = "finastra.camel.esb.route.request.enable.SftpPkToAmq";
    String ENABLE_ROUTE_RESPONSE_FROM_VENDOR_AMQ_TO_SFTP_PK = "finastra.camel.esb.route.response.enable.AmqToSftpPk";

    String VENDOR_NAME = "finastra.camel.esb.vendor.name";
    String RESPONSE_SYNC = "finastra.camel.esb.response.sync";
    String RESPONSE_AUTHENTICATED_PATHS = "quarkus.http.auth.permission.authenticated.paths";
    //rest
    String REST_PERIOD = "finastra.camel.esb.?.rest.period";
    String REST_AUTH_URL = "finastra.camel.esb.?.rest.auth_url";
    String REST_GRANT_TYPE = "finastra.camel.esb.?.rest.grant_type";
    String REST_SCOPE = "finastra.camel.esb.?.rest.scope";
    String REST_USERNAME = "finastra.camel.esb.?.rest.username";
    String REST_PASSWORD = "finastra.camel.esb.?.rest.password";
    String REST_CLIENT_ID = "finastra.camel.esb.?.rest.client_id";
    String REST_CLIENT_SECRET = "finastra.camel.esb.?.rest.client_secret";
    String REQUEST_REST_METHOD = "finastra.camel.esb.rest.request.method";
    String REQUEST_REST_HEADERS = "finastra.camel.esb.request.rest.headers"; // key1=value1,key2=value2
    String REQUEST_REST_URL = "finastra.camel.esb.rest.request.url";
    String RESPONSE_REST_URL = "finastra.camel.esb.response.rest.url";
    //amq
    String P2G_AMQ_CONNECTION_FACTORY = "finastra.camel.p2g.amq.connectionFactory";
    String VENDOR_AMQ_CONNECTION_FACTORY = "finastra.camel.vendor.amq.connectionFactory";
    String REQUEST_P2G_AMQ_QUEUE_NAME = "finastra.camel.p2g.amq.request.queue.name";
    String REQUEST_VENDOR_AMQ_QUEUE_NAME = "finastra.camel.vendor.amq.request.queue.name";
    String RESPONSE_P2G_AMQ_QUEUE_NAME = "finastra.camel.p2g.amq.response.queue.name";
    String RESPONSE_VENDOR_AMQ_QUEUE_NAME = "finastra.camel.vendor.amq.response.queue.name";
    String SECOND_RESPONSE_P2G_AMQ_QUEUE_NAME = "finastra.camel.p2g.amq.second.response.queue.name";
    //ibmmq
    String VENDOR_IBM_CONNECTION_FACTORY = "finastra.camel.vendor.ibmmq.connectionFactory";
    String RESPONSE_VENDOR_IBMMQ_QUEUE_NAME = "finastra.camel.vendor.ibmmq.response.queue.name";
    String REQUEST_VENDOR_IBMMQ_QUEUE_NAME = "finastra.camel.vendor.ibmmq.request.queue.name";
    //sftp
    String SFTP_ADDRESS = "finastra.camel.p2g.sftp.address";
    String SFTP_TO_FOLDER = "finastra.camel.p2g.sftp.toFolder";
    String SFTP_USERNAME = "finastra.camel.p2g.sftp.username";
    String SFTP_PASSWORD = "finastra.camel.p2g.sftp.password";
    String SFTP_PRIVATE_KEY_FILE = "finastra.camel.p2g.sftp.privateKeyFile";
    String SFTP_FROM_FOLDER = "finastra.camel.p2g.sftp.fromFolder";
    String SFTP_ARCHIVE_FOLDER = "finastra.camel.p2g.sftp.archiveFolder";
    String SFTP_SPLIT_ALGO = "finastra.camel.p2g.sftp.splitalgo";
    String SFTP_SPLIT_TAG = "finastra.camel.p2g.sftp.splittag";

    default String resolveProperty(String property) {
        return "{{%s}}".formatted(property);
    }
    default String resolveProperty(String property,String replacement) {
        return "{{%s}}".formatted(property.replace("?", replacement.toLowerCase()));
    }
}
