package com.example.emergen_app.presentation.branch

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.R
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.data.models.User
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.presentation.components.AppHeader
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.ui.theme.colorCardIcon

@Composable
fun BranchScreen(
    navController: NavController
) {
    val viewModel: BranchViewModel = hiltViewModel()
    val branch by viewModel.branch.collectAsState()
    val helpRequests by viewModel.helpRequests.collectAsState()

    var selectedStatus by remember { mutableStateOf("Being Processed") }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFDF1D0))
                .padding(bottom = 26.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {}
        AppHeader()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFDF1D0))
                .padding(bottom = 26.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {}
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp)
    ) {
        branch?.let { branch ->
            BranchCard(navController = navController, branch = branch)
            viewModel.getFilteredReports(branch.typeBranch)
        }

        // أزرار الفلترة مع الشكل المطلوب
        FilterButtons(selectedStatus) { status ->
            selectedStatus = status
        }

        val filteredRequests = helpRequests.filter { it.statusRequest == selectedStatus }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn {
                itemsIndexed(filteredRequests) { _, request ->
                    HelpRequestItem(request, onStatusUpdate = { userId, currentStatus ->
                        viewModel.updateRequestStatus(userId, currentStatus)
                    }) { userId ->
                        Log.d("HelpRequestItem", "Navigating to user details: $userId")
                        navController.navigate("user_details/$userId")  // الانتقال إلى صفحة تفاصيل المستخدم
                    }

                }
            }
        }
    }
}

@Composable
fun FilterButtons(selectedStatus: String, onStatusSelected: (String) -> Unit) {
    val statuses = listOf("Being Processed", "Team On Way", "Completed")

    Row(
        modifier = Modifier
            .background(Color(0xFFF2FBFE))
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        statuses.forEach { status ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onStatusSelected(status) }
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = status,
                    color = if (status == selectedStatus) Color(0xFF009EFF) else Color(0x993987A4),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (status == selectedStatus) {
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .fillMaxWidth(fraction = 0.3f)
                            .background(
                                Color(0xFF009EFF),
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFilterButtons() {
    var selectedStatus by remember { mutableStateOf("Being Processed") }

    FilterButtons(selectedStatus) { status ->
        selectedStatus = status
    }
}


@Composable
fun BranchCard(
    navController: NavController,
    branch: Branch,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(Color(0xFF22B8F5)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .background(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.group_371),
                        contentDescription = "Branch Image",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row {
                    Text(
                        text = "Hello!,",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = branch.branchName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colorCardIcon)
            ) {
                IconButton(onClick = {
                    navController.navigate(AppDestination.BranchInfoDestination.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White
                    )
                }
            }

        }
    }
}

@Composable
fun HelpRequestItem(
    request: User,
    onStatusUpdate: (String, String) -> Unit,
    onImageClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .background(Color.White)
                        .clickable { onImageClick(request.userId) } // عند الضغط على الصورة
                ) {
                    Image(
                        painter = rememberImagePainter(request.userPhoto),
                        contentDescription = "User Photo",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                ) {
                    Text(text = request.userName, style = MaterialTheme.typography.bodySmall)
                    Text(text = request.mobile, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.weight(1f))
                LocationText(location = request.addressMaps)
            }
            Spacer(modifier = Modifier.height(8.dp))

            AddressCard(
                label = "Address",
                value = "${request.governmentName}, ${request.area}, ${request.plotNumber}," +
                        " ${request.streetName}, ${request.buildNumber}, ${request.floorNumber}, ${request.apartmentNumber}"
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(Color.Gray)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = request.typeOfRequest,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp
            )
            Text(
                text = if (request.typeReason.isNotEmpty()) request.typeReason else request.textOther,
                color = Color.Black,
                fontSize = 14.sp
            )
            Text(
                text = request.timeOfRequest,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )

            val statusText = when (request.statusRequest) {
                "Being Processed" -> "Send Team"
                "Team On Way" -> "Team Arrived"
                else -> request.statusRequest
            }

            val backgroundColor =
                if (request.statusRequest == "Completed") Color.Gray else adminWelcomeCard

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        onStatusUpdate(
                            request.userId,
                            request.statusRequest
                        )
                    }, // تحديث الحالة عند الضغط
                shape = RoundedCornerShape(5.dp),
            ) {
                Column(
                    modifier = Modifier
                        .background(backgroundColor)
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AddressCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun LocationText(location: String) {
    var locationText by remember { mutableStateOf(location) }
    val context = LocalContext.current

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.End
    ) {
        IconButton(
            onClick = {
                val uri = "geo:$locationText?q=$locationText"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Open in Maps",
                tint = adminWelcomeCard
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHelpRequestItem() {
    val sampleRequest = User(
        typeOfRequest = "Medical Emergency",
        typeReason = "Cardiac Arrest",
        textOther = "",
        timeOfRequest = "2023-10-01 12:34:56",
        statusRequest = "Being Processed",
        userName = "Ahmed Mohamed Ali",
        mobile = "1234567890",
    )
    HelpRequestItem(
        request = sampleRequest,
        onStatusUpdate = { _, _ -> },
        onImageClick = { }
    )
}