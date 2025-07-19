package io.github.shared;

import io.github.shared.annotations.ToStateLog;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A container for various ID's encapsulation within a single idempotent class.
 */
public class Ids {

    @ToStateLog(includeNullValue = false)
    private final String requestId;
    @ToStateLog(includeNullValue = false)
    private final String correlationId;
    @ToStateLog(includeNullValue = false)
    private final String traceId;
    @ToStateLog(includeNullValue = false)
    private final String spanId;
    @ToStateLog(includeNullValue = false)
    private final String transactionId;
    @ToStateLog(includeNullValue = false)
    private final String userId;
    @ToStateLog(includeNullValue = false)
    private final String tenantId;
    @ToStateLog(includeNullValue = false)
    private final String operationId;
    @ToStateLog(includeNullValue = false)
    private final String parentId;
    @ToStateLog(includeNullValue = false)
    private final String messageId;
    @ToStateLog(includeNullValue = false)
    private final String eventId;
    @ToStateLog(includeNullValue = false)
    private final String clientId;
    @ToStateLog(includeNullValue = false)
    private final String deviceId;
    @ToStateLog(includeNullValue = false)
    private final String applicationId;


    public static Ids create() {
        return new Ids(null, null,null,null,null,null,null,null,null,null,null,null,null,null);
    }

    public static Ids fromOther(Ids other) {
        return new Ids(
                other.getRequestId(),
                other.getCorrelationId(),
                other.getTraceId(),
                other.getSpanId(),
                other.getTransactionId(),
                other.getUserId(),
                other.getTenantId(),
                other.getOperationId(),
                other.getParentId(),
                other.getMessageId(),
                other.getEventId(),
                other.getClientId(),
                other.getDeviceId(),
                other.getApplicationId()
                );
    }

    public static Ids fromHttpHeaders(Map<String, String> headers) {
        return new Ids(
                headers.get("X-Request-Id"),
                headers.get("X-Correlation-Id"),
                headers.get("X-Trace-Id"),
                headers.get("X-Span-Id"),
                headers.get("X-Transaction-Id"),
                headers.get("X-User-Id"),
                headers.get("X-Tenant-Id"),
                headers.get("X-Operation-Id"),
                headers.get("X-Parent-Id"),
                headers.get("X-Message-Id"),
                headers.get("X-Event-Id"),
                headers.get("X-Client-Id"),
                headers.get("X-Device-Id"),
                headers.get("X-Application-Id")
        );
    }



    public Ids withRequestId() {
        return withRequestId(UUID.randomUUID().toString());
    }

    public Ids withRequestId(final String requestId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withCorrelationId() {
        return withCorrelationId(UUID.randomUUID().toString());
    }

    public Ids withCorrelationId(final String correlationId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withTraceId() {
        return withTraceId(UUID.randomUUID().toString());
    }

    public Ids withTraceId(final String traceId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withSpanId() {
        return withSpanId(UUID.randomUUID().toString());
    }

    public Ids withSpanId(final String spanId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withTransactionId() {
        return withTransactionId(UUID.randomUUID().toString());
    }

    public Ids withTransactionId(final String transactionId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withUserId() {
        return withUserId(UUID.randomUUID().toString());
    }

    public Ids withUserId(final String userId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withTenantId() {
        return withTenantId(UUID.randomUUID().toString());
    }

    public Ids withTenantId(final String tenantId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withOperationId() {
        return withOperationId(UUID.randomUUID().toString());
    }

    public Ids withOperationId(final String operationId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withParentId() {
        return withParentId(UUID.randomUUID().toString());
    }

    public Ids withParentId(final String parentId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withMessageId() {
        return withMessageId(UUID.randomUUID().toString());
    }

    public Ids withMessageId(final String messageId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withEventId() {
        return withEventId(UUID.randomUUID().toString());
    }

    public Ids withEventId(final String eventId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withClientId() {
        return withClientId(UUID.randomUUID().toString());
    }

    public Ids withClientId(final String clientId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withDeviceId() {
        return withDeviceId(UUID.randomUUID().toString());
    }

    public Ids withDeviceId(final String deviceId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public Ids withApplicationId() {
        return withApplicationId(UUID.randomUUID().toString());
    }

    public Ids withApplicationId(final String applicationId) {
        return new Ids(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getOperationId() {
        return operationId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void injectToMDC() {
        toMap().forEach((key, value) -> MDC.put(key, value.toString()));
    }

    public Ids fromMDC() {
        return new Ids(
                MDC.get("requestId"),
                MDC.get("correlationId"),
                MDC.get("traceId"),
                MDC.get("spanId"),
                MDC.get("transactionId"),
                MDC.get("userId"),
                MDC.get("tenantId"),
                MDC.get("operationId"),
                MDC.get("parentId"),
                MDC.get("messageId"),
                MDC.get("eventId"),
                MDC.get("clientId"),
                MDC.get("deviceId"),
                MDC.get("applicationId")
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> idsMap = new HashMap<>();

        if (requestId != null) idsMap.put("requestId", requestId);
        if (correlationId != null) idsMap.put("correlationId", correlationId);
        if (traceId != null) idsMap.put("traceId", traceId);
        if (spanId != null) idsMap.put("spanId", spanId);
        if (transactionId != null) idsMap.put("transactionId", transactionId);
        if (userId != null) idsMap.put("userId", userId);
        if (tenantId != null) idsMap.put("tenantId", tenantId);
        if (operationId != null) idsMap.put("operationId", operationId);
        if (parentId != null) idsMap.put("parentId", parentId);
        if (messageId != null) idsMap.put("messageId", messageId);
        if (eventId != null) idsMap.put("eventId", eventId);
        if (clientId != null) idsMap.put("clientId", clientId);
        if (deviceId != null) idsMap.put("deviceId", deviceId);
        if (applicationId != null) idsMap.put("applicationId", applicationId);

        return idsMap;
    }

    public Map<String, String> toHttpHeaders() {
        Map<String, String> headers = new HashMap<>();

        if (requestId != null) headers.put("X-Request-Id", requestId);
        if (correlationId != null) headers.put("X-Correlation-Id", correlationId);
        if (traceId != null) headers.put("X-Trace-Id", traceId);
        if (spanId != null) headers.put("X-Span-Id", spanId);
        if (transactionId != null) headers.put("X-Transaction-Id", transactionId);
        if (userId != null) headers.put("X-User-Id", userId);
        if (tenantId != null) headers.put("X-Tenant-Id", tenantId);
        if (operationId != null) headers.put("X-Operation-Id", operationId);
        if (parentId != null) headers.put("X-Parent-Id", parentId);
        if (messageId != null) headers.put("X-Message-Id", messageId);
        if (eventId != null) headers.put("X-Event-Id", eventId);
        if (clientId != null) headers.put("X-Client-Id", clientId);
        if (deviceId != null) headers.put("X-Device-Id", deviceId);
        if (applicationId != null) headers.put("X-Application-Id", applicationId);

        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ids)) return false;
        Ids ids = (Ids) o;
        return Objects.equals(requestId, ids.requestId) && Objects.equals(correlationId, ids.correlationId) && Objects.equals(traceId, ids.traceId) && Objects.equals(spanId, ids.spanId) && Objects.equals(transactionId, ids.transactionId) && Objects.equals(userId, ids.userId) && Objects.equals(tenantId, ids.tenantId) && Objects.equals(operationId, ids.operationId) && Objects.equals(parentId, ids.parentId) && Objects.equals(messageId, ids.messageId) && Objects.equals(eventId, ids.eventId) && Objects.equals(clientId, ids.clientId) && Objects.equals(deviceId, ids.deviceId) && Objects.equals(applicationId, ids.applicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, correlationId, traceId, spanId, transactionId, userId, tenantId, operationId, parentId, messageId, eventId, clientId, deviceId, applicationId);
    }

    @Override
    public String toString() {
        return toMap().toString();
    }


    private Ids(
            String requestId,
            String correlationId,
            String traceId,
            String spanId,
            String transactionId,
            String userId,
            String tenantId,
            String operationId,
            String parentId,
            String messageId,
            String eventId,
            String clientId,
            String deviceId,
            String applicationId) {

        this.requestId = requestId;
        this.correlationId = correlationId;
        this.traceId = traceId;
        this.spanId = spanId;
        this.transactionId = transactionId;
        this.userId = userId;
        this.tenantId = tenantId;
        this.operationId = operationId;
        this.parentId = parentId;
        this.messageId = messageId;
        this.eventId = eventId;
        this.clientId = clientId;
        this.deviceId = deviceId;
        this.applicationId = applicationId;
    }
}
