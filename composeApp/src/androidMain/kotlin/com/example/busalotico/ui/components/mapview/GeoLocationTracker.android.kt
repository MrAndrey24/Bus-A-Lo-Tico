package com.example.busalotico.ui.components.mapview

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController

actual fun getLocationTracker(permissionsController: PermissionsController): LocationTracker {
    return LocationTracker(
        permissionsController = permissionsController
    )
}