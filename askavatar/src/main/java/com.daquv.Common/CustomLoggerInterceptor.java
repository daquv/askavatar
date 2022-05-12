package com.daquv.Common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import static okhttp3.internal.platform.Platform.INFO;

public class CustomLoggerInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public interface Logger {
        void log(String message);

        /** A {@link Logger} defaults output appropriate for the current platform. */
        Logger DEFAULT = new Logger() {
            @Override public void log(String message) {
                Platform.get().log(message, INFO, null);
            }
        };
    }
    private final Logger logger;
    public CustomLoggerInterceptor() {
        this(Logger.DEFAULT);
    }
    public CustomLoggerInterceptor(Logger logger) {
        this.logger = logger;
    }

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Response intercept(Chain chain) throws IOException {
        StringBuilder log = new StringBuilder();
        log.append(" Send Retrofit ===>");
        log.append("\r\n");

        // Request Part
        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Connection connection = chain.connection();

        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        log.append(requestStartMessage);
        log.append("\r\n");

        if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                log.append("Content-Type: ");
                log.append(requestBody.contentType());
                log.append("\r\n");
            }
            if (requestBody.contentLength() != -1) {
                log.append("Content-Length: ");
                log.append(requestBody.contentLength());
                log.append("\r\n");
            }
        }
        // Header Part
        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if ( !"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name) && !"Authorization".equalsIgnoreCase(name) ) {
                if(i == 0){
                    log.append(" ");
                }else if(i > 0){
                    log.append(", ");
                }
                log.append("[");
                log.append(name);
                log.append("] : ");
                log.append(headers.value(i));
                log.append("\r\n");
            }
        }

        // Response Part
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.log("<-- HTTP FAILED: " + e);
            throw e;
        }

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        log.append("\r\n");
        log.append("<-- ");
        log.append(response.code());
        log.append((response.message().isEmpty() ? "" : ' ' + response.message()));
        log.append(' ');
        log.append(response.request().url());
        log.append("\r\n");
        Headers resheaders = response.headers();
        for (int i = 0, count = resheaders.size(); i < count; i++) {//Response Header 출력
            if(i == 0){
                log.append(" ");
            }else if(i > 0){
                log.append(", ");
            }
            log.append("[");
            log.append(resheaders.name(i));
            log.append("] : ");
            log.append(resheaders.value(i));
            log.append("\r\n");
        }
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Long gzippedLength = null;
        if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
            gzippedLength = buffer.size();
            GzipSource gzippedResponseBody = null;
            try {
                gzippedResponseBody = new GzipSource(buffer.clone());
                buffer = new Buffer();
                buffer.writeAll(gzippedResponseBody);
            } finally {
                if (gzippedResponseBody != null) {
                    gzippedResponseBody.close();
                }
            }
        }
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        if (!isPlaintext(buffer)) {
//            logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
            return response;
        }

        if (contentLength != 0) {
            String responseBodyString = buffer.clone().readString(charset);
            try{
//                System.out.println("==============>>> responseBodyString : "+ responseBodyString);
//                Map result = objectMapper.readValue(responseBodyString, new TypeReference<Object>(){});
                log.append("\r\n");
                log.append("Response : ");
                log.append(responseBodyString);
                log.append("\r\n");
            }catch(Exception e){
                e.printStackTrace();
                return response;
            }
        }

        logger.log(log.toString());

        return response;
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
