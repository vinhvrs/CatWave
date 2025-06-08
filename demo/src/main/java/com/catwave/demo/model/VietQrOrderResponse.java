package com.catwave.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VietQrOrderResponse {
    private String bankCode;
    private String bankName;
    private String bankAccount;
    private String userBankName;
    private String amount;
    private String content;
    private String qrCode;
    private String imgId;
    private Integer existing;
    private String transactionId;
    private String transactionRefId;
    private String qrLink;
    private String terminalCode;
    private String subTerminalCode;
    private String serviceCode;
    private String orderId;
    private List<Object> additionalData;

    // Nếu API có cấu trúc bao gồm resultInfo, bạn có thể thêm:
    // private ResultInfo resultInfo;
    //
    // @JsonIgnoreProperties(ignoreUnknown = true)
    // public static class ResultInfo {
    //     private String status;
    //     private String message;
    //     // getters/setters
    // }

    // --- getters + setters ---
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }

    public String getUserBankName() { return userBankName; }
    public void setUserBankName(String userBankName) { this.userBankName = userBankName; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public String getImgId() { return imgId; }
    public void setImgId(String imgId) { this.imgId = imgId; }

    public Integer getExisting() { return existing; }
    public void setExisting(Integer existing) { this.existing = existing; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getTransactionRefId() { return transactionRefId; }
    public void setTransactionRefId(String transactionRefId) { this.transactionRefId = transactionRefId; }

    public String getQrLink() { return qrLink; }
    public void setQrLink(String qrLink) { this.qrLink = qrLink; }

    public String getTerminalCode() { return terminalCode; }
    public void setTerminalCode(String terminalCode) { this.terminalCode = terminalCode; }

    public String getSubTerminalCode() { return subTerminalCode; }
    public void setSubTerminalCode(String subTerminalCode) { this.subTerminalCode = subTerminalCode; }

    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public List<Object> getAdditionalData() { return additionalData; }
    public void setAdditionalData(List<Object> additionalData) {
        this.additionalData = additionalData;
    }
}
