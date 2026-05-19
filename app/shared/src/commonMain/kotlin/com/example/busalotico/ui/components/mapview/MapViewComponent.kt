/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop

@Composable
expect fun MapViewComponent(
    modifier: Modifier = Modifier,
    coordinate: Coordinate,
    recenterTrigger: Int,
    zoomInTrigger: Int = 0,
    zoomOutTrigger: Int = 0,
    stops: List<RouteStop> = emptyList(),
    polylinePoints: List<Coordinate> = emptyList(),
    busLocation: Coordinate?,
    flyToLocation: Coordinate? = null,
)
