package com.example.busalotico.ui.screen

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
import com.example.busalotico.ui.components.SelectRouteButton
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        MapViewComponent(
            coordinate = coordinate // If there are no permissions, the coordinate will be null and the map will display the default view.
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
