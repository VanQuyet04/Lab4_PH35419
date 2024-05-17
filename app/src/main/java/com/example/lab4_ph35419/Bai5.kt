package com.example.lab4_ph35419

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class PaymentMethod(
    val name: String, val iconResId: Int, val color: Color
)

sealed class Screen(val route: String, val icon: Int, val title: String) {
    object Home : Screen("home", R.drawable.ic_home, "Trang chủ")
    object History : Screen("history", R.drawable.ic_history, "Lịch sử")
    object Cart : Screen("cart", R.drawable.ic_cart, "Giỏ hàng")
    object Profile : Screen("profile", R.drawable.ic_profile, "Hồ sơ")
}

class Bai5 : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            var selectedMethod by remember { mutableStateOf<PaymentMethod?>(null) }

            // Tạo 1 list gồm các PaymentMethod component là các item row chứa: Ảnh, tên phương thức, radiobutton tích chọn
            val paymentMethods = listOf(
                PaymentMethod("PayPal", R.drawable.ic_paypal, Color(0xFFFFA726)),
                PaymentMethod("Momo", R.drawable.ic_momo, Color(0xFFF48FB1)),
                PaymentMethod("Zalo Pay", R.drawable.ic_zalopay, Color(0xFF81D4FA)),
                PaymentMethod("Thanh toán trực tiếp", R.drawable.ic_zalopay, Color(0xFF80CBC4))
            )

//            thành phần xem như container bao bọc các component con bên trong, tự sắp xếp để chúng k bị đè lên nhau
            Scaffold(

                // top bar chứa nút back, title và menu(CÓ thể sử dụng TopAppBar mặc định.Ở đây là tự custom)
                topBar = {
                    TopToolbar()
                },
                // thành phần BottomNavigationBar
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }

            ) {
//                NavigationBar(Bottom)
                NavigationGraph(navController = navController)

//                danh sách phương thức thanh toán
                PaymentMethodList(
                    paymentMethods = paymentMethods,
                    selectedMethod = selectedMethod,
                    onMethodSelected = { selectedMethod = it }
                )

            }


        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopToolbar() {

        TopAppBar(
            title = { Text(text = "Thanh Toán") },

            navigationIcon = {
                IconButton(onClick = { /* Xử lý nút quay lại */ }) {
                    Icon(painterResource(id = R.drawable.ic_back), contentDescription = "Back")
                }
            },

            actions = {
                IconButton(onClick = { /* Xử lý hành động khác */ }) {
                    Icon(painterResource(id = R.drawable.menu), contentDescription = "Other Action")
                }
            },

            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                navigationIconContentColor = Color.Black,
                actionIconContentColor = Color.Black
            )

        )

    }

    @Composable
    fun AddressSection() {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Quyết | Xóm 7 Khu 15", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "51 Kim Hoàng An Trai Đường Ngã Tư Canh",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Vân Canh Hoài Đức", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Nam Từ Liêm, Thành phố Hà Nội",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

// Main content

    @Composable
    fun PaymentMethodList(
        paymentMethods: List<PaymentMethod>,
        selectedMethod: PaymentMethod?,
        onMethodSelected: (PaymentMethod) -> Unit
    ) {
        Column {
            Text(
                text = "Địa chỉ nhận hàng",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )
            // Display Address (this can be a separate composable)
            AddressSection()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Vui lòng chọn một trong những phương thức sau:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn {
                items(paymentMethods) { method ->
                    PaymentMethodItem(
                        paymentMethod = method,
                        selected = selectedMethod == method,
                        onSelected = { onMethodSelected(method) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle next step */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Tiếp theo")
            }
        }
    }


    @Composable
    fun PaymentMethodItem(
        paymentMethod: PaymentMethod,
        selected: Boolean,
        onSelected: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(paymentMethod.color, RoundedCornerShape(8.dp))
                .clickable { onSelected() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = paymentMethod.iconResId),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = paymentMethod.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            RadioButton(
                selected = selected,
                onClick = { onSelected() }
            )
        }
    }


    //    ----------------

    //    các màn để chuyến hướng trong bottom nav bar
    @Composable
    fun HomeScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        }
    }

    @Composable
    fun HistoryScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Lịch sử", style = MaterialTheme.typography.titleSmall)
        }
    }

    @Composable
    fun CartScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Giỏ hàng", style = MaterialTheme.typography.titleSmall)
        }
    }

    @Composable
    fun ProfileScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Hồ sơ", style = MaterialTheme.typography.titleSmall)
        }
    }


    //     hàm tạo thành phần bottom nav bar. Trong đó kết hợp sử dụng NavigationBar và NavigationBarItem
    @Composable
    fun BottomNavigationBar(navController: NavHostController) {

        // Tạo list dựa vào các object đã khai báo ở main
        val items = listOf(
            Screen.Home,
            Screen.History,
            Screen.Cart,
            Screen.Profile
        )

        NavigationBar(
            containerColor = Color.White
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { screen ->
                NavigationBarItem(

                    icon = {
                        Icon(
                            painterResource(id = screen.icon),
                            contentDescription = screen.title
                        )
                    },
                    label = { Text(screen.title) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Điều hướng đến một màn hình duy nhất, không tạo thêm bản sao
                            launchSingleTop = true
                            // Khôi phục trạng thái đã lưu
                            restoreState = true
                            // Xóa tất cả các trang trước trang đích để tránh chồng chất trang
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Red,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Red,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {

        NavHost(navController, startDestination = Screen.Home.route) {

            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.History.route) { HistoryScreen() }
            composable(Screen.Cart.route) { CartScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }

        }

    }


}