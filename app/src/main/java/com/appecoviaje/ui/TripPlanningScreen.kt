package com.appecoviaje.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.appecoviaje.R
import com.appecoviaje.data.Trip
import com.appecoviaje.theme.AppEcoViajeTheme
import com.appecoviaje.viewmodel.TripPlanningViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPlanningScreen(
    navController: NavController,
    tripPlanningViewModel: TripPlanningViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val uiState by tripPlanningViewModel.uiState.collectAsState()
    var showOnlyFavorites by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var locationText by remember { mutableStateOf("Ubicación no disponible") }
    var currentCity by remember { mutableStateOf("Desconocida") }
    var userLocation by remember { mutableStateOf<android.location.Location?>(null) }

    val fusedLocationClient = remember {
        com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLocation = location
                        locationText = "Lat: ${location.latitude}, Lon: ${location.longitude}"
                        
                        // Geocoder logic
                        try {
                            val geocoder = android.location.Geocoder(context, java.util.Locale.getDefault())
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                                    if (addresses.isNotEmpty()) {
                                        currentCity = addresses[0].locality ?: addresses[0].subAdminArea ?: "Desconocida"
                                    }
                                }
                            } else {
                                @Suppress("DEPRECATION")
                                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                if (!addresses.isNullOrEmpty()) {
                                    currentCity = addresses[0].locality ?: addresses[0].subAdminArea ?: "Desconocida"
                                }
                            }
                        } catch (e: Exception) {
                            currentCity = "Error al obtener ciudad"
                        }
                    } else {
                        locationText = "Ubicación no encontrada"
                    }
                }
            } catch (e: SecurityException) {
                locationText = "Permiso denegado"
            }
        } else {
            locationText = "Permiso denegado"
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val tripsToShow = if (showOnlyFavorites) {
        uiState.trips.filter { it.isFavorite }
    } else {
        uiState.trips
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planificación de Viajes") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }
                },
                actions = {
                    FilterChip(
                        selected = showOnlyFavorites,
                        onClick = { showOnlyFavorites = !showOnlyFavorites },
                        label = { Text("Favoritos") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (showOnlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Filter Favorites"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Search and Filter Section
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                OutlinedTextField(
                    value = tripPlanningViewModel.searchQuery.collectAsState().value,
                    onValueChange = { tripPlanningViewModel.onSearchQueryChange(it) },
                    label = { Text("Buscar viajes") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                val categories = listOf("Montaña", "Desierto", "Isla", "Ciudad", "Valle")
                val selectedCategory = tripPlanningViewModel.selectedCategory.collectAsState().value
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { tripPlanningViewModel.onCategorySelected(null) },
                            label = { Text("Todos") }
                        )
                    }
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { tripPlanningViewModel.onCategorySelected(category) },
                            label = { Text(category) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            // Location Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = locationText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ciudad más cercana: $currentCity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            if (tripsToShow.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (showOnlyFavorites) "No tienes viajes favoritos." else "Cargando viajes...")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tripsToShow, key = { it.id }) { trip ->
                        // Calculate distance
                        var distanceString = "Calculando..."
                        if (userLocation != null) {
                            val tripLocation = android.location.Location("").apply {
                                latitude = trip.latitude
                                longitude = trip.longitude
                            }
                            val distanceInMeters = userLocation!!.distanceTo(tripLocation)
                            val distanceInKm = (distanceInMeters / 1000).toInt()
                            distanceString = "A $distanceInKm km de distancia"
                        }

                        TripItem(
                            trip = trip, 
                            distanceText = distanceString,
                            onFavoriteClick = { tripPlanningViewModel.toggleFavorite(trip) },
                            onRouteClick = {
                                val gmmIntentUri = android.net.Uri.parse("geo:0,0?q=${android.net.Uri.encode(trip.location)}")
                                val mapIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                try {
                                    context.startActivity(mapIntent)
                                } catch (e: Exception) {
                                    val webIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri)
                                    context.startActivity(webIntent)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip, distanceText: String, onFavoriteClick: () -> Unit, onRouteClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = trip.imageResId),
                contentDescription = "Image of ${trip.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = trip.title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (trip.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Mark as Favorite",
                            tint = if (trip.isFavorite) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = trip.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Ubicación: ${trip.location}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = distanceText, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRouteClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cómo llegar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripPlanningScreenPreview() {
    AppEcoViajeTheme {
        val sampleTrip = Trip(
            id = 1,
            title = "Torres del Paine",
            description = "Icónicas montañas de granito y lagos turquesa.",
            location = "Parque Nacional Torres del Paine",
            latitude = -51.0,
            longitude = -73.0,
            imageResId = R.drawable.paisaje_torres_del_paine, // Corrected: Added missing parameter
            category = "Montaña",
            isFavorite = true
        )
        TripItem(trip = sampleTrip, distanceText = "A 2500 km de distancia", onFavoriteClick = {}, onRouteClick = {})
    }
}
