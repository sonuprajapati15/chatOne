package com.example.chatone.enums;

public enum RequestCode {

    CAMERA_PERMISSION_CODE(101, "CAMERA_PERMISSION_CODE"),
    STORAGE_PERMISSION_CODE(102, "STORAGE_PERMISSION_CODE"),
    ACCESS_COARSE_LOCATION(103, "ACCESS_COARSE_LOCATION"),
    ACCESS_FINE_LOCATION(104, "ACCESS_FINE_LOCATION"),
    SEND_SMS(105, "SEND_SMS"),
    READ_EXTERNAL_STORAGE(106, "READ_EXTERNAL_STORAGE"),
    WRITE_EXTERNAL_STORAGE(107, "WRITE_EXTERNAL_STORAGE"),
    GOOGLE_SIGN_IN(108, "GOOGLE_SIGN_IN"),
    CAMERA_ACTION(109, "CAMERA_ACTION"),
    ACCESS_NETWORK_STATE(110, "ACCESS_NETWORK_STATE");

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    Integer codeId;
    String desc;

    RequestCode(int codeId, String desc) {
        this.codeId = codeId;
        this.desc = desc;
    }

    public static RequestCode getEnumById(int codeId) {
        for (RequestCode requestCode : RequestCode.values()) {
            if(requestCode.getCodeId() == codeId){
                return requestCode;
            }
        }
        return null;
    }
}
