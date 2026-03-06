package com.example.busalotico.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.busalotico.ui.components.RecenterButton
import com.example.busalotico.ui.components.SelectRouteButton
import com.example.busalotico.ui.components.ZoomControls
import com.example.busalotico.ui.components.mapview.MapViewComponent
import com.example.busalotico.ui.components.searchBar.SearchBar
import com.example.shared.domain.entities.Coordinate

@Composable
fun HomeScreen(
    coordinate: Coordinate,
    modifier: Modifier = Modifier,
    onSelectRouteClick: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }
    var recenterTrigger by remember { mutableStateOf(0) }
    var zoomInTrigger by remember { mutableStateOf(0) }
    var zoomOutTrigger by remember { mutableStateOf(0) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        MapViewComponent(
            coordinate = coordinate, // If there are no permissions, the coordinate will be null and the map will display the default view.
            recenterTrigger = recenterTrigger,
            zoomInTrigger = zoomInTrigger,
            zoomOutTrigger = zoomOutTrigger
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 120.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ZoomControls(
                onZoomIn = { zoomInTrigger++ },
                onZoomOut = { zoomOutTrigger++ }
            )
            RecenterButton(
                onClick = { recenterTrigger++ }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                SelectRouteButton(onClick = onSelectRouteClick)
            }
        }
    }
}