package com.yellobook.api.support.logging;

import com.yellobook.api.support.logging.dto.HttpLogMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

// 로깅의 종류가 많잖아여
// http 로깅
// slf4j 처럼 로그찍는
// 데이터베이스 쿼리 로깅설정

// 이 모듈이 이걸 다 가지고 있다라고 하면
// http 로깅이 필요없는 모듈 core 에서는 slf4J 로깅만 필요한데
// 이 의존성을 implement 받게 되는 문제가 존재한다.
// implement project(":supprot:logging")
// support - 상위모듈
//  - logging - 상위모듈
//   - http-loging
//   - db-logging
//   -
// 당장에는 필요없으니 일단 다 만들고 필요해지면 분리하자

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class ReqResLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "request_id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        /**
         * HTTP 요청과 응답의 본문은 InputStream 과 OutputStream 을 통해 처리되기 때문에 한 번만 읽거나 쓸 수 있다.
         * 스트림을 한번 읽으면 스트림의 포인터가 데이터 끝으로 이동하며 다시 데이터를 읽을 수 없게 된다.
         *
         * 예를 들어, 로그에 기록하고 싶다면 이를 한번 읽어야 하는데
         * 읽으면 스트림이 끝에 도달해 클라이언트에게 응답을 보낼 수 없게 되는 문제가 발생한다.
         *
         *  따라서 ContentCachingRequestWrapper 와 ContentCachingResponseWrapper 를 이용해
         *  요청 및 응답의 본문을 캐싱해 여러번 읽을 수 있도록 한다.
         *
         *  ContentCachingRequestWrapper 와 ContentCachingResponseWrapper 는 내부적으로 요청 본문을 밭이트 배열에 저장하고
         *  필요할 때마다 배열에서 데이터를 읽어 반환한다.
         *
         *  ContentCachingResponseWrapper 코드를 보면 copyBodyToResponse() 메서드가 있는데
         *  클라이언트에게 응답을 전송할 때 캐싱된 데이터를 실제 응답에 복사하는 역할을 한다.
         */
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        // request id 식별자 추가
        String requestId = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        MDC.put(REQUEST_ID, requestId);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);
        long endTime = System.currentTimeMillis();

        double elapsedTime = (endTime - startTime) / 1000.0;

        try {
            HttpLogMessage logMessage = HttpLogMessage.createInstance(cachingRequestWrapper, cachingResponseWrapper,
                    elapsedTime);
            log.info(logMessage.toPrettierLog());

            cachingResponseWrapper.copyBodyToResponse();
        } catch (Exception e) {
            log.error("Logging failed", e);
        } finally {
            MDC.remove(REQUEST_ID);
        }
    }

    /**
     * Swagger 에 대한 HTTP 로깅을 비활성하기 위한 설정
     * <p>
     * doFilterInternal 메서드가 호출되기 전에 souldNotFilter 메서드를 호출해 필터를 적용할지 결정한다. shouldNotFilter 메서드가 false 를 반환하면
     * doFilterInternal 는 호출되지 않고 해당 요청에 대해 필터가 적용되지 않는다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.equals("/swagger-ui.html");
    }
}
