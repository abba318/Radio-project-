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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

data class NewsArticle(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val summary: String,
    val fullContent: String
)

@Composable
fun NewsroomScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    var expandedArticleId by remember { mutableStateOf<String?>(null) }

    val categories = listOf("All", "Governance", "Development", "Community", "Sports", "Culture")

    val articles = remember {
        listOf(
            NewsArticle(
                id = "news_1",
                title = "Niger State Government Accelerates Rural Infrastructure & Farmer Input Distribution",
                category = "Governance",
                date = "Today • 08:30 AM",
                summary = "State ministries announce high-yield seed subsidies and mechanized tractor leasing programs across Bida, Kontagora, and Suleja agricultural zones.",
                fullContent = "Speaking during a live broadcast on BCNS Crystal FM 91.3 Minna, state government representatives outlined targeted interventions in agricultural extension services, solar-powered boreholes, and road rehabilitations aimed at bolstering grain yields and food security throughout Niger State."
            ),
            NewsArticle(
                id = "news_2",
                title = "Broadcasting Corporation of Niger State Upgrades Transmitter Network to Boost Regional Reach",
                category = "Development",
                date = "Yesterday • 04:15 PM",
                summary = "BCNS engineers complete maintenance and power modernization at booster transmission sites covering Mokwa and New Bussa.",
                fullContent = "The modernization initiative ensures crystal clear audio coverage on 91.3 FM for motorists along the Kaduna-Minna-Mokwa expressway and provides uninterrupted civic broadcasting to grassroots rural communities in the Nupe, Gbagyi, and Kambari belts."
            ),
            NewsArticle(
                id = "news_3",
                title = "Niger Tornadoes FC Gear Up for NPFL Home Fixture with Intense Training Session",
                category = "Sports",
                date = "Yesterday • 11:00 AM",
                summary = "The Ikon Allah Boys conduct final tactical drills ahead of their weekend encounter at the Bako Kontagora Stadium in Minna.",
                fullContent = "Catch live pre-match commentary, interviews with team captains, and full tactical breakdowns during the Sports Arena (Filin Wasanni) programme airing at 4:30 PM on Crystal FM 91.3."
            ),
            NewsArticle(
                id = "news_4",
                title = "Annual Niger State Cultural Festival Celebrates Nupe and Gbagyi Heritage",
                category = "Culture",
                date = "2 days ago",
                summary = "Traditional rulers, artisans, and youth troupes converge in Minna to exhibit historic brass crafts, oral folklore, and indigenous music.",
                fullContent = "Crystal FM 91.3 provides live exterior outside broadcasting coverage of the colorful durbar parades and handicraft exhibitions as part of its public service mandate to safeguard regional cultural patrimony."
            ),
            NewsArticle(
                id = "news_5",
                title = "Public Health Advisory: Maternal & Child Healthcare Registration Open Across 25 LGAs",
                category = "Community",
                date = "3 days ago",
                summary = "Free immunization and antenatal care packages available at primary health centers throughout Niger State.",
                fullContent = "The State Primary Healthcare Development Agency urges families to visit nearest health clinics for routine vaccinations. Detailed schedules will be read out during the Karatun Jaridu and Women & Society broadcasts."
            )
        )
    }

    val filteredArticles = remember(selectedCategory) {
        if (selectedCategory == "All") articles else articles.filter { it.category == selectedCategory }
    }

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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
        ) {
        // Newsroom Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF092236)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3397B1C7))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Newspaper,
                            contentDescription = null,
                            tint = BcnsCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "BCNS NEWSROOM & UPDATES",
                            color = BcnsCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Verified Grassroots News & Regional Bulletins",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Stay informed with accurate, authoritative reportage on governance, civic affairs, agriculture, and sports from across Niger State.",
                        color = Color(0xFF9CB8CD),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Category Filter Row
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) BcnsCyan else Color(0xFF071B2A))
                            .border(1.dp, if (isSelected) BcnsCyan else Color(0x3397B1C7), RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color(0xFF041420) else Color(0xFFBED2E2),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Articles List
        items(filteredArticles, key = { it.id }) { article ->
            val isExpanded = expandedArticleId == article.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expandedArticleId = if (isExpanded) null else article.id
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF081C2C)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x2297B1C7))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF0D2F48))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = article.category,
                                color = BcnsCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = article.date,
                            color = Color(0xFF8AA8BF),
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = article.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isExpanded) article.fullContent else article.summary,
                        color = Color(0xFFA5C2D6),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isExpanded) "Show Less ▲" else "Read Full Story ▼",
                            color = BcnsGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "${article.title}\n\nRead more on BCNS Crystal FM 91.3 Minna:\nhttps://bcnsradioniger.org.ng")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share News"))
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color(0xFF8AA8BF),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
}
