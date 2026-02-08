package com.mdt.mindustry.utils.exception;

import com.mdt.common.signal.Failure;

public sealed interface WorldLoadException extends Failure {

    record InvalidInputException(String message) implements WorldLoadException {

    }

    record WorldLoadRuntimeException(Exception cause) implements WorldLoadException {

    }
}
