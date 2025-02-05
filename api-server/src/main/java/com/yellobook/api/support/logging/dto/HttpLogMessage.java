package com.yellobook.api.support.logging.dto;

import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class HttpLogMessage {
    private final String httpMethod;
    private final String requestUri;
    private final HttpStatus httpStatus;
    private final String clientIp;
    private final double elapsedTime;
    private final String headers;
    private final String requestParam;
    private final String requestBody;
    private final String responseBody;

    public HttpLogMessage(String httpMethod, String requestUri, HttpStatus httpStatus, String clientIp,
                          double elapsedTime,
                          String headers, String requestParam, String requestBody, String responseBody) {
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
        this.httpStatus = httpStatus;
        this.clientIp = clientIp;
        this.elapsedTime = elapsedTime;
        this.headers = headers;
        this.requestParam = requestParam;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
    }

    public static HttpLogMessage createInstance(ContentCachingRequestWrapper requestWrapper,
                                                ContentCachingResponseWrapper responseWrapper, double elapsedTime) {
        String headers = Collections.list(requestWrapper.getHeaderNames())
                .stream()
                .map(header -> header + ": " + requestWrapper.getHeader(header))
                .collect(Collectors.joining(", "));

        String requestBody = new String(requestWrapper.getContentAsByteArray());
        String responseBody = new String(responseWrapper.getContentAsByteArray());

        return new HttpLogMessage(
                requestWrapper.getMethod(),
                requestWrapper.getRequestURI(),
                HttpStatus.valueOf(responseWrapper.getStatus()),
                requestWrapper.getRemoteAddr(),
                elapsedTime,
                headers,
                requestWrapper.getQueryString(),
                requestBody,
                responseBody
        );
    }

    public String toPrettierLog() {
        return String.format(
                """
                                                
                        |[REQUEST] %s %s %s (%.3f)
                        |>> CLIENT_IP: %s
                        |>> HEADERS: %s
                        |>> REQUEST_PARAM: %s
                        |>> REQUEST_BODY: %s
                        |>> RESPONSE_BODY: %s
                        """,
                this.httpMethod,
                this.requestUri,
                this.httpStatus,
                this.elapsedTime,
                this.clientIp,
                this.headers,
                this.requestParam,
                this.requestBody,
                this.responseBody
        );
    }
}
