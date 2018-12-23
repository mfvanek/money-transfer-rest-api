package com.mfvanek.money.transfer.utils;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.ToString;

public final class JsonUtils {

    private JsonUtils() {}

    public static Gson make() {
        return new Gson().newBuilder().setPrettyPrinting().create();
    }

    public static String toJson(Exception err, int errCode) {
        final ErrorInfo info = new ErrorInfo(err, errCode);
        return make().toJson(info);
    }

    @Getter
    @ToString
    private static class ErrorInfo {

        private final int errorCode;
        private final String errorMessage;

        ErrorInfo(Exception err, int errCode) {
            this.errorCode = errCode;
            this.errorMessage = err.getLocalizedMessage();
        }
    }
}
