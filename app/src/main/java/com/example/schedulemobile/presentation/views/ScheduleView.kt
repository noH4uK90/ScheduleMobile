package com.example.schedulemobile.presentation.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schedulemobile.R
import com.example.schedulemobile.presentation.customComponents.OutputPair
import com.example.schedulemobile.presentation.navigation.NavRoute
import com.example.schedulemobile.presentation.viewModels.ObserverLifecycle
import com.example.schedulemobile.presentation.viewModels.ScheduleViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun ScheduleView(
    navController: NavController,
    viewModel: ScheduleViewModel
) {
    val pagerState = rememberPagerState()
    var currentDay: String? by rememberSaveable { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val items = DrawerItems.values()

    viewModel.ObserverLifecycle(LocalLifecycleOwner.current.lifecycle)

    val currentTimetableList = viewModel.state.currentTimetableList?.items

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                items.forEach {
                    Spacer(Modifier.height(12.dp))
                    NavigationDrawerItem(
                        icon = { Icon(painterResource(it.icon), contentDescription = null) },
                        label = { Text(it.itemName) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(it.route)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier,
                    title = {
                        Text(
                            text = viewModel.state.currentTimetableList?.items?.first()?.group?.name ?: "",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Black
                        )
                    },
                    navigationIcon = {
                        Icon(
                            painterResource(R.drawable.menu),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .clickable {
                                    scope.launch { drawerState.open() }
                                }
                        )
                    },

                    )
            },
            content = { paddingValues ->
                HorizontalPager(
                    count = 6,
                    state = pagerState,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    userScrollEnabled = true
                ) {
                    currentTimetableList?.map {
                        currentDay = it.days[currentPage].key.name
                        it.days[currentPage].items.map { timetable ->
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp, vertical = 15.dp)
                                    .border(
                                        width = 2.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(15.dp)
                                    )
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    text = currentDay ?: "",
                                    textAlign = TextAlign.Center,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight(600)
                                )
                                Card(
                                    modifier = Modifier
                                        .padding(vertical = 15.dp, horizontal = 15.dp)
                                ) {
                                    LazyColumn(
                                        content = {
                                            timetable.pairs.map { pair ->
                                                item { OutputPair(pair = pair) }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

enum class DrawerItems {
    GROUP {
        override val itemName = "Группа"
        override val icon = R.drawable.group
        override val route = NavRoute.GroupScreen.route
    },
    SETTINGS {
        override val itemName = "Настройки"
        override val icon = R.drawable.settings
        override val route = NavRoute.SettingsScreen.route
    };

    abstract val itemName: String
    abstract val icon: Int
    abstract val route: String
}