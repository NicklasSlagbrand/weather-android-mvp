package dk.nicklasslagbrand.forecast.utils.extension

fun String.toTitleCase(): String {
    return Character.toUpperCase(this[0]) +
            this.substring(1).toLowerCase();
}