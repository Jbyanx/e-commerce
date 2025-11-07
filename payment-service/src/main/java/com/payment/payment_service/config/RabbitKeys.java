package com.payment.payment_service.config;

public final class RabbitKeys {

    private RabbitKeys() {}

    // Exchange
    public static final String ORDER_EXCHANGE = "order.exchange";

    // Routing keys
    public static final String INVENTORY_RESERVE = "inventory.reserve";
    public static final String INVENTORY_RESERVED = "inventory.reserved";
    public static final String INVENTORY_REJECTED = "inventory.rejected";
    public static final String INVENTORY_RELEASE = "inventory.release";

    public static final String PAYMENT_PROCESS = "payment.process";
    public static final String PAYMENT_COMPLETED = "payment.completed";
    public static final String PAYMENT_FAILED = "payment.failed";

    // Queue names
    public static final String INVENTORY_RESERVE_QUEUE = "inventory.reserve.queue";
    public static final String INVENTORY_RELEASE_QUEUE = "inventory.release.queue";
    public static final String INVENTORY_EVENTS_QUEUE = "inventory.events";

    public static final String PAYMENT_COMMAND_QUEUE = "payment.command.queue";
    public static final String PAYMENT_EVENTS_QUEUE = "payment.events";
    public static final String PAYMENT_EVENTS_FAILED_QUEUE = "payment.events.failed";
}