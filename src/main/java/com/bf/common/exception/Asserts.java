package com.bf.common.exception;

import com.bf.common.api.ErrorCode;

public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(ErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

}
