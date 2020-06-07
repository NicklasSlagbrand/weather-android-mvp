package dk.nicklasslagbrand.forecast.api.exception

import java.lang.Exception

class EmptyResponseBodyException : Exception() {
    override fun toString(): String {
        return "Response has no body"
    }
}
