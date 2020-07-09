package dk.nicklasslagbrand.forecast.data.exception

import java.lang.Exception

class UnSuccessfullResponseException (message: String) : Exception(message) {
    override fun toString(): String {
        return "Response was unsuccessful: (errorCode=$message)"
    }
}
