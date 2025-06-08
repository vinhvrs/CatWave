package com.catwave.demo.model;

/**
 * Represents a single transaction that the client
 * will POST to /payment/api/transactions/sync.
 */
public class TransactionDto {
    private String orderId;
    private Long amount;
    private String description;   // map → content
    private Integer qrType;       // 0=dynamic,1=static,3=semi‐dynamic
    private String transType;     // "C" hoặc "D", mặc định "C"
    private String terminalCode;  // nếu static hoặc semi‐dynamic
    private String serviceCode;   // nếu semi‐dynamic
    private String returnUrl;     // urlLink

    public TransactionDto() {}

    public TransactionDto(String orderId, Long amount, String description, Integer qrType, String transType, String terminalCode, String serviceCode, String returnUrl) {
        this.orderId = orderId;
        this.amount = amount;
        this.description = description;
        this.qrType = qrType;
        this.transType = transType;
        this.terminalCode = terminalCode;
        this.serviceCode = serviceCode;
        this.returnUrl = returnUrl;
    }
        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getQrType() {
            return qrType;
        }

        public void setQrType(Integer qrType) {
            this.qrType = qrType;
        }

        public String getTransType() {
            return transType;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public String getTerminalCode() {
            return terminalCode;
        }

        public void setTerminalCode(String terminalCode) {
            this.terminalCode = terminalCode;
        }

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }
}
