package com.markosopcic.cycler.location

import android.location.Location

interface ILocationListener {
    fun nextLocation(location: Location?)
}