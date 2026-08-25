package com.kavishka.kavishkamart.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Filter that generates a unique X-Request-ID for tracing and attaches it to SLF4J MDC.
 * Satisfies Spec Section 18 Observability requirements.
 */
public class MdcLoggingFilter implements Filter {
    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String MDC_KEY = "requestId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpServletRequest) {
            String requestId = httpServletRequest.getHeader(REQUEST_ID_HEADER);
            if (requestId == null || requestId.trim().isEmpty()) {
                requestId = UUID.randomUUID().toString();
            }
            MDC.put(MDC_KEY, requestId);

            if (response instanceof HttpServletResponse httpServletResponse) {
                httpServletResponse.setHeader(REQUEST_ID_HEADER, requestId);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    @Override
    public void destroy() {
    }
}

