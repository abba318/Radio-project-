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
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import java.text.NumberFormat
import java.util.Locale

data class RateCardItem(
    val title: String,
    val durationOrType: String,
    val primeRate: Long,
    val standardRate: Long,
    val description: String
)

@Composable
fun AdvertRatesScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val rateCards = remember {
        listOf(
            RateCardItem(
                title = "30-Second Commercial Jingle",
                durationOrType = "30 Seconds Audio Jingle",
                primeRate = 18500L,
                standardRate = 14000L,
                description = "High-rotation audio advert during Morning Drive (06:30-09:30 AM) and Evening Drive (05:00-08:00 PM)."
            ),
            RateCardItem(
                title = "60-Second Full Feature Jingle",
                durationOrType = "60 Seconds Audio Jingle",
                primeRate = 28000L,
                standardRate = 22000L,
                description = "Complete brand narrative with background jingle, call-to-action, and contact details."
            ),
            RateCardItem(
                title = "Live Presenter Mention (Hype)",
                durationOrType = "60-90 Seconds Live Hype",
                primeRate = 25000L,
                standardRate = 19000L,
                description = "Organic, persuasive on-air endorsement delivered directly by on-air anchors during top shows."
            ),
            RateCardItem(
                title = "Sponsored 30-Minute Programme",
                durationOrType = "30 Minutes Studio Time",
                primeRate = 95000L,
                standardRate = 75000L,
                description = "Interactive talk show format with studio guest interviews and live phone-in questions from listeners."
            ),
            RateCardItem(
                title = "Public & Government Announcement",
                durationOrType = "Up to 150 Words Text",
                primeRate = 15000L,
                standardRate = 12000L,
                description = "Formal announcements read out in English, Hausa, and Nupe during bulletin roundups."
            ),
            RateCardItem(
                title = "Outside Broadcast (OB Van) Coverage",
                durationOrType = "Live On-Site Broadcast",
                primeRate = 280000L,
                standardRate = 220000L,
                description = "Complete outdoor transmission van, live audio feed, and presenter crew covering your corporate, cultural, or state event."
            )
        )
    }

    var selectedItemIndex by remember { mutableIntStateOf(0) }
    var spotCount by remember { mutableIntStateOf(5) }
    var isPrimeTime by remember { mutableStateOf(true) }

    val activeRate = rateCards[selectedItemIndex]
    val unitPrice = if (isPrimeTime) activeRate.primeRate else activeRate.standardRate
    val totalPrice = unitPrice * spotCount

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BcnsNightBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // Advert Rates Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF092033)),
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
                            text = "COMMERCIAL RATE CARD",
                            color = BcnsGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Broadcast Advertising That Delivers Results",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Reach over 3 million verified listeners across Niger State and North-Central Nigeria. Jingles, sponsorships, hypes, and live event coverage.",
                        color = Color(0xFF9CB8CD),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Interactive Rate Estimator Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF071926)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BcnsCyan.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = BcnsCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Instant Campaign Estimator",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Selected Package: ${activeRate.title}",
                        color = BcnsCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Peak Time vs Standard Time Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isPrimeTime) BcnsCyan else Color(0xFF0C2436))
                                .clickable { isPrimeTime = true }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Prime Time (Drive)",
                                color = if (isPrimeTime) Color(0xFF041420) else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (!isPrimeTime) BcnsCyan else Color(0xFF0C2436))
                                .clickable { isPrimeTime = false }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Standard Time",
                                color = if (!isPrimeTime) Color(0xFF041420) else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Spot Count selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Number of Spots:",
                            color = Color(0xFF90AFC5),
                            fontSize = 12.sp
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(1, 3, 5, 10, 20).forEach { count ->
                                val isSelected = count == spotCount
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) BcnsGold else Color(0xFF0C2436))
                                        .clickable { spotCount = count }
                                        .padding(horizontal = 9.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "$count",
                                        color = if (isSelected) Color(0xFF1E1400) else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Calculation Summary Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF040F18))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Estimated Cost ($spotCount spots)",
                                    color = Color(0xFF8AA8BF),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = formatNairaAmount(totalPrice),
                                    color = BcnsGold,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Button(
                                onClick = {
                                    bookCampaignWhatsApp(
                                        context = context,
                                        item = activeRate,
                                        spots = spotCount,
                                        timeType = if (isPrimeTime) "Prime Time" else "Standard Time",
                                        total = formatNairaAmount(totalPrice)
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BcnsSuccess),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Book via WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Rate Card Items List
        item {
            Text(
                text = "All Commercial Packages",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(rateCards.indices.toList()) { index ->
            val rate = rateCards[index]
            val isSelected = index == selectedItemIndex

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedItemIndex = index },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFF0D283E) else Color(0xFF081C2C)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) BcnsCyan else Color(0x2297B1C7)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = rate.title,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "${formatNairaAmount(rate.primeRate)} / spot",
                            color = BcnsGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = rate.durationOrType,
                        color = BcnsCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = rate.description,
                        color = Color(0xFF90AFC5),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

private fun formatNairaAmount(amount: Long): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "₦" + formatter.format(amount)
}

private fun bookCampaignWhatsApp(context: Context, item: RateCardItem, spots: Int, timeType: String, total: String) {
    try {
        val message = "Hello BCNS Crystal FM 91.3 Commercial Desk,\nI would like to book an advertising campaign:\n- Package: ${item.title}\n- Slots: $spots spots\n- Timing: $timeType\n- Total Estimate: $total\n\nPlease advise on schedule availability and broadcast materials."
        val url = "https://api.whatsapp.com/send?phone=2348036914274&text=" + Uri.encode(message)
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (_: Exception) {}
}
