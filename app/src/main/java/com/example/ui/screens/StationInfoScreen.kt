package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BcnsCyan
import com.example.ui.theme.BcnsGold
import com.example.ui.theme.BcnsNightBg
import com.example.ui.theme.BcnsSuccess

@Composable
fun StationInfoScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var senderName by remember { mutableStateOf("") }
    var requestType by remember { mutableStateOf("Song Request") }
    var studioMessage by remember { mutableStateOf("") }

    val requestTypes = listOf("Song Request", "Birthday Shoutout", "General Greeting", "Advert Inquiry")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BcnsNightBg),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // About Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF092033)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3397B1C7))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = BcnsCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "About BCNS Radio",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Broadcasting Corporation of Niger State (BCNS) is the official public service broadcast media outfit of the Niger State Government in Nigeria, operating Crystal FM 91.3 Minna as its flagship radio channel.",
                        color = Color(0xFFB5CCE0),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "With booster stations in Minna, Bida, Kontagora, Mokwa, and New Bussa, BCNS broadcasts trusted grassroots news, educational programming, peace advocacy, and cultural preservation in English, Hausa, Nupe, and Gbagyi.",
                        color = Color(0xFF90AFC5),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // WhatsApp Studio Interaction Composer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF062B1D)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4412B886))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(BcnsSuccess),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Send Studio Request via WhatsApp",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Send shout-outs directly to on-air presenters",
                                color = Color(0xFFA5DFC7),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Request type pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        requestTypes.forEach { type ->
                            val isSelected = type == requestType
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) BcnsSuccess else Color(0xFF041E14))
                                    .clickable { requestType = type }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = type,
                                    color = if (isSelected) Color(0xFF01140C) else Color(0xFFC0EBD9),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = senderName,
                        onValueChange = { senderName = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Your Name & Location (e.g. Amina from Minna)", color = Color(0xFF679B84)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = BcnsSuccess,
                            unfocusedBorderColor = Color(0x5512B886),
                            focusedContainerColor = Color(0xFF02170E),
                            unfocusedContainerColor = Color(0xFF02170E)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = studioMessage,
                        onValueChange = { studioMessage = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Your shoutout, greetings, or song dedication message...", color = Color(0xFF679B84)) },
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = BcnsSuccess,
                            unfocusedBorderColor = Color(0x5512B886),
                            focusedContainerColor = Color(0xFF02170E),
                            unfocusedContainerColor = Color(0xFF02170E)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val name = if (senderName.isBlank()) "A loyal listener" else senderName.trim()
                            val msg = if (studioMessage.isBlank()) "Greetings to the BCNS team!" else studioMessage.trim()
                            val formatted = "BCNS STUDIO REQUEST\nType: $requestType\nFrom: $name\nMessage: $msg"
                            val url = "https://api.whatsapp.com/send?phone=2348036914274&text=" + Uri.encode(formatted)
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BcnsSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Send via WhatsApp (0803 691 4274)", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Commercial Advertising & Rate Card Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF081C2C)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3397B1C7))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = BcnsGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Commercial Opportunities & Advert Rates",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Reach over 3 million active listeners across north-central Nigeria and the global diaspora.",
                        color = Color(0xFF9AB7CC),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RateItem("30-sec Commercial Jingle", "Prime Breakfast & Drive Slots")
                    RateItem("Live Presenter Mention (Hype)", "Natural on-air endorsement")
                    RateItem("Sponsored 30-min Program", "Interactive live phone-in format")
                    RateItem("Government & Public Announcements", "Multi-language broadcast")
                }
            }
        }

        // Contact details Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF071926)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3397B1C7))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Contact & Studio Lines",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ContactRow(
                        icon = Icons.Default.LocationOn,
                        title = "Studio Headquarters",
                        detail = "Broadcasting Road, P.M.B. 88, Minna, Niger State",
                        tint = BcnsCyan,
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ContactRow(
                        icon = Icons.Default.Phone,
                        title = "Direct Studio Hotline",
                        detail = "0803 691 4274",
                        tint = BcnsGold,
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+2348036914274"))
                            context.startActivity(intent)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ContactRow(
                        icon = Icons.Default.Email,
                        title = "Official Inquiries",
                        detail = "radioniger1@gmail.com",
                        tint = BcnsCyan,
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:radioniger1@gmail.com"))
                            context.startActivity(intent)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ContactRow(
                        icon = Icons.Default.Language,
                        title = "Official Website",
                        detail = "https://bcnsradioniger.org.ng",
                        tint = BcnsGold,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bcnsradioniger.org.ng"))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
}

@Composable
private fun RateItem(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color(0xFF8AA8BF), fontSize = 11.sp)
        }
        Text("Contact Desk", color = BcnsGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    detail: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = tint, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, color = Color(0xFF8AA8BF), fontSize = 11.sp)
            Text(detail, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}
