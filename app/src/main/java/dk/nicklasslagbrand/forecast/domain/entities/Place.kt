package dk.nicklasslagbrand.forecast.domain.entities

import dk.nicklasslagbrand.forecast.data.repository.mapping.Location

data class Place(val woeId: String,
                 val city: String,
                 val location: Location,
                 val country: String,
                 val temperature: Temperature,
                 val weatherCode: Int)