package com.lyn.lost_and_found.config.constant;

/**
 * 交易状态： 0-等待同意 1-交易成功 2-交易失败 3-交易进行中
 */
public enum RecordStatus {
    WAITING_PERMISSION,
    SUCCESS,
    FAIL,
    TRANSACTION
}
