package com.paulniu.audio_collection.AudioRecorder;

/**
 * 录音对象的状态
 */

public enum AudioStatus {
    //未开始
    STATUS_NO_READY,
    //预备
    STATUS_READY,
    //录音
    STATUS_START,
    //暂停
    STATUS_PAUSE,
    //停止
    STATUS_STOP
}
