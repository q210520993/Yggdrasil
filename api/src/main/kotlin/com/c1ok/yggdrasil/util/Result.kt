package com.c1ok.yggdrasil.util

@JvmRecord
data class Result<T>(val success: T, val reason: Reason) {
    companion object {
        fun isSuccess(result: Result<Any>): Boolean {
            return result.reason is Reason.Success
        }
    }
}

sealed class Reason {

    data class Failed(val reason: String) : Reason()

    data object Success : Reason()

}