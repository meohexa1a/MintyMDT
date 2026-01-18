package com.mdt.mindustry.utils.exception;

import com.mdt.common.shared.signal.Failure;

public sealed interface WorldLoadException extends Failure {

    record InvalidInputException(String message) implements WorldLoadException {

    }

    record WorldLoadRuntimeException(Throwable cause) implements WorldLoadException {

    }
}
