package com.c1ok.yggdrasil.util

@JvmRecord
data class Result<T>(val success: T, val reason: Reason) {
    companion object {
        fun isSuccess(result: Result<Any>): Boolean {
            return result.reason is Reason.Success
        }

        fun createFailed(reason: String): Result<Boolean> {
            return Result<Boolean>(false, Reason.Failed(reason))
        }

        fun createSuccess(): Result<Boolean> {
            return Result(true, Reason.Success)
        }

    }
}

sealed class Reason {

    data class Failed(val reason: String) : Reason() {
        override fun toString(): String = reason
    }

    data object Success : Reason()

}