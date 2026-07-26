package com.example.ui.screens.servers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.Server
import com.example.ui.components.ServerCard
import com.example.ui.theme.*

enum class ServerFilterTab { ALL, FASTEST, RECOMMENDED, FAVORITES }

@Composable
fun ServersScreen(
    servers: List<Server>,
    selectedServerId: String?,
    onSelectServer: (Server) -> Unit,
    onToggleFavorite: (Server) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(ServerFilterTab.ALL) }

    val filteredServers = remember(servers, searchQuery, selectedFilter) {
        servers.filter { server ->
            val matchesQuery = server.serverName.contains(searchQuery, ignoreCase = true) ||
                    server.countryName.contains(searchQuery, ignoreCase = true) ||
                    server.city.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                ServerFilterTab.ALL -> true
                ServerFilterTab.FASTEST -> server.latencyMs in 1..40
                ServerFilterTab.RECOMMENDED -> server.isRecommended
                ServerFilterTab.FAVORITES -> server.isFavorite
            }

            matchesQuery && matchesFilter
        }.sortedBy { if (selectedFilter == ServerFilterTab.FASTEST) it.latencyMs else 0 }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(stringResource(R.string.search_servers), color = TextMuted, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonCyan) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = GlassBorder
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("server_search_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Pills Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(ServerFilterTab.values()) { filter ->
                val isSelected = selectedFilter == filter
                val label = when (filter) {
                    ServerFilterTab.ALL -> stringResource(R.string.filter_all)
                    ServerFilterTab.FASTEST -> stringResource(R.string.filter_fastest)
                    ServerFilterTab.RECOMMENDED -> stringResource(R.string.filter_recommended)
                    ServerFilterTab.FAVORITES -> stringResource(R.string.filter_favorites)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) NeonCyan else DarkSurface)
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) ElectricPurple else TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredServers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No servers found",
                    color = TextMuted,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(filteredServers, key = { it.id }) { server ->
                    ServerCard(
                        server = server,
                        isSelected = server.id == selectedServerId,
                        onSelectServer = { onSelectServer(server) },
                        onToggleFavorite = { onToggleFavorite(server) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
