package com.rafi.esb.infra.routes.config;

public abstract class RouteFields {
    public static final String SEDA_CAMEL ="seda:%s-%s";
    public static final String AMQ_CAMEL = "activemq:queue:";
    public static final String IBMMQ_CAMEL = "jms:queue:";
    public static final String SFTP_CAMEL_WITH_PASSWORD = "sftp://%s%s?username=%s&password=%s";
    public static final String SFTP_CAMEL_WITH_PRIVATE_KEY = "sftp://%s%s?username=%s&privateKeyFile=%s&useUserKnownHostsFile=false&strictHostKeyChecking=no";
    public static final String SFTP_CAMEL_FILE_NAME = "&filename=%s-%s.txt";
    public static final String SFTP_CAMEL_MOVE ="&move=%s/${file:name.noext}-%s.${file:ext}";
    public static final String SFTP_FILE_NAME_DATE_FORMAT = "${date:now:yyyyMMddHHmmssSSS}";
    public static final String MQ_CONNECTION = "%s%s?connectionFactory=#%s";
    public static final String AUTH = "Authorization";
    public static final String HTTP_CONTENT_TYPE = "application/json";
    public static final String BODY_CONTENT = "body: ${body}";

}
