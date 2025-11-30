package com.appecoviaje.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appecoviaje.viewmodel.ReservationDetails
import com.appecoviaje.viewmodel.ReservationViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    navController: NavController,
    reservationViewModel: ReservationViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val reservationDetails by reservationViewModel.reservationDetails.collectAsState()
    val trips by reservationViewModel.trips.collectAsState()
    
    var selectedTripId by remember { mutableStateOf<Int?>(null) }
    var tripExpanded by remember { mutableStateOf(false) }
    
    val adventureTypes = listOf("Alojamiento", "Tour", "Transporte")
    var selectedAdventureType by remember { mutableStateOf(adventureTypes[0]) }
    var typeExpanded by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    val showDatePicker = remember { mutableStateOf(false) }

    LaunchedEffect(trips) {
        if (selectedTripId == null) {
            selectedTripId = trips.firstOrNull()?.id
        }
    }

    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = { 
                    datePickerState.selectedDateMillis?.let { 
                        selectedDate = it
                    }
                    showDatePicker.value = false 
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mis Reservas") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            
            // Trip Selector
            ExposedDropdownMenuBox(expanded = tripExpanded, onExpandedChange = { tripExpanded = !tripExpanded }) {
                TextField(
                    value = trips.find { it.id == selectedTripId }?.title ?: "Selecciona un viaje",
                    onValueChange = {}, readOnly = true, label = { Text("Viaje a reservar") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tripExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = tripExpanded, onDismissRequest = { tripExpanded = false }) {
                    trips.forEach { trip ->
                        DropdownMenuItem(text = { Text(trip.title) }, onClick = { selectedTripId = trip.id; tripExpanded = false })
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Adventure Type Selector
            ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = !typeExpanded }) {
                TextField(
                    value = selectedAdventureType, onValueChange = {}, readOnly = true, label = { Text("Tipo de Aventura") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                    adventureTypes.forEach { type ->
                        DropdownMenuItem(text = { Text(type) }, onClick = { selectedAdventureType = type; typeExpanded = false })
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Date Picker Button
            OutlinedButton(onClick = { showDatePicker.value = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.CalendarMonth, contentDescription = "Select Date", modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Fecha del Evento: ${formatDate(selectedDate, "dd MMM yyyy")}")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { 
                    if (selectedDate < System.currentTimeMillis() - 86400000) { // Allow today (subtract 24h buffer roughly or just check day)
                         scope.launch { snackbarHostState.showSnackbar("La fecha no puede ser en el pasado.") }
                    } else {
                        selectedTripId?.let { id ->
                            navController.navigate("payment/$id/$selectedDate/$selectedAdventureType")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedTripId != null
            ) {
                Text("Confirmar Reserva")
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            if (reservationDetails.isEmpty()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("Aún no tienes reservas.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(reservationDetails, key = { it.reservationId }) { details ->
                        ReservationItem(details = details) {
                            reservationViewModel.deleteReservation(details.reservationId)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReservationItem(details: ReservationDetails, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = details.tripTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = details.adventureType, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(text = "Fecha: ${formatDate(details.eventDate, "dd MMM yyyy")}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Reservado el: ${formatDate(details.reservationDate, "dd/MM/yy")}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Reserva", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

private fun formatDate(timestamp: Long, format: String): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(Date(timestamp))
}
