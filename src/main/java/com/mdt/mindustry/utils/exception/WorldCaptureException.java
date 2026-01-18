package com.mdt.mindustry.utils.exception;

import com.mdt.common.signal.Failure;

public sealed interface WorldCaptureException extends Failure {

    // !---------------------------------------------------!

    record WorldClosedException() implements WorldCaptureException {

    }

    record InvalidInputException(String message) implements WorldCaptureException {

    }

    record WorldCaptureRuntimeException(Throwable cause) implements WorldCaptureException {

    }
}
