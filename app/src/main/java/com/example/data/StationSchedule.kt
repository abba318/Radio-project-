package com.example.data

import java.util.Calendar
import java.util.TimeZone

data class RadioProgram(
    val id: String,
    val title: String,
    val host: String,
    val timeSlot: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val category: String,
    val description: String
)

object StationSchedule {

    val programs = listOf(
        RadioProgram(
            id = "prog_1",
            title = "Morning Devotion & National Anthem",
            host = "Studio Duty Announcer",
            timeSlot = "05:30 AM - 06:30 AM",
            startHour = 5,
            startMinute = 30,
            endHour = 6,
            endMinute = 30,
            category = "Spiritual & Civic",
            description = "Station sign-on, uplifting morning prayers, national anthem, and inspirational meditation."
        ),
        RadioProgram(
            id = "prog_2",
            title = "The Morning Drive (Idon Mikiya)",
            host = "Aliyu Garba & Maryam Sani",
            timeSlot = "06:30 AM - 09:30 AM",
            startHour = 6,
            startMinute = 30,
            endHour = 9,
            endMinute = 30,
            category = "Breakfast & News",
            description = "Flagship breakfast show featuring traffic watch across Minna, weather reports, regional headlines, and live listener comments."
        ),
        RadioProgram(
            id = "prog_3",
            title = "Newspaper Review (Karatun Jaridu)",
            host = "Mallam Ibrahim Zubairu",
            timeSlot = "09:30 AM - 10:00 AM",
            startHour = 9,
            startMinute = 30,
            endHour = 10,
            endMinute = 0,
            category = "Analysis & Press",
            description = "Detailed reading and contextual breakdown of major national and northern Nigerian daily newspaper headlines."
        ),
        RadioProgram(
            id = "prog_4",
            title = "Matters of State (Gwamnati Da Jama'a)",
            host = "Hajiya Fatima Bello",
            timeSlot = "10:00 AM - 12:00 PM",
            startHour = 10,
            startMinute = 0,
            endHour = 12,
            endMinute = 0,
            category = "Governance & Public Policy",
            description = "In-depth conversation on Niger State development initiatives, agriculture policies, healthcare projects, and phone-in discussions with state commissioners."
        ),
        RadioProgram(
            id = "prog_5",
            title = "Mid-Day News Bulletin & Markets",
            host = "BCNS Central Newsroom",
            timeSlot = "12:00 PM - 01:00 PM",
            startHour = 12,
            startMinute = 0,
            endHour = 13,
            endMinute = 0,
            category = "Newsroom",
            description = "Comprehensive regional news bulletin covering Minna, Bida, Kontagora, Suleja, and market commodity prices."
        ),
        RadioProgram(
            id = "prog_6",
            title = "Women, Youth & Society",
            host = "Zainab Ahmed",
            timeSlot = "01:00 PM - 02:00 PM",
            startHour = 13,
            startMinute = 0,
            endHour = 14,
            endMinute = 0,
            category = "Community & Lifestyle",
            description = "Empowerment dialogues, maternal health, youth skill acquisition in Niger State, and family wellbeing."
        ),
        RadioProgram(
            id = "prog_7",
            title = "Request Time (Zabi Naka)",
            host = "DJ Crystal & Salisu Usman",
            timeSlot = "02:00 PM - 04:30 PM",
            startHour = 14,
            startMinute = 0,
            endHour = 16,
            endMinute = 30,
            category = "Music & Requests",
            description = "High-energy afternoon show playing audience song requests, studio greetings, shout-outs, and contemporary Afrobeat & Hausa classics."
        ),
        RadioProgram(
            id = "prog_8",
            title = "Sports Arena (Filin Wasanni)",
            host = "Babatunde Lawal",
            timeSlot = "04:30 PM - 06:00 PM",
            startHour = 16,
            startMinute = 30,
            endHour = 18,
            endMinute = 0,
            category = "Sports",
            description = "Live coverage of Niger Tornadoes FC, grassroots leagues across the 25 LGAs, NPFL highlights, and European football updates."
        ),
        RadioProgram(
            id = "prog_9",
            title = "Major Evening News & World Report",
            host = "Senior Duty Editors",
            timeSlot = "06:00 PM - 07:30 PM",
            startHour = 18,
            startMinute = 0,
            endHour = 19,
            endMinute = 30,
            category = "Newsroom",
            description = "The principal evening news broadcast with live correspondence from Government House Minna and rural community reporters."
        ),
        RadioProgram(
            id = "prog_10",
            title = "Cultural Heritage & Niger Folktales",
            host = "Alhaji Mamman Kolo",
            timeSlot = "07:30 PM - 09:30 PM",
            startHour = 19,
            startMinute = 30,
            endHour = 21,
            endMinute = 30,
            category = "Culture & Heritage",
            description = "Celebrating the rich tapestry of Nupe, Gbagyi, Hausa, and Kambari traditions, proverbs, oral history, and indigenous music."
        ),
        RadioProgram(
            id = "prog_11",
            title = "Night Breeze & Smooth Melodies",
            host = "Late Night Host",
            timeSlot = "09:30 PM - 12:00 AM",
            startHour = 21,
            startMinute = 30,
            endHour = 24,
            endMinute = 0,
            category = "Late Night Music",
            description = "Relaxing late-night broadcast, soul melodies, listener dedications, and quiet thoughts before station sign-off."
        )
    )

    fun getCurrentProgram(): RadioProgram {
        // Nigerian West Africa Time (WAT = UTC+1)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"))
        val currentMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)

        for (p in programs) {
            val start = p.startHour * 60 + p.startMinute
            val end = p.endHour * 60 + p.endMinute
            if (currentMinutes in start until end) {
                return p
            }
        }
        return programs[1] // Default to Morning Drive
    }
}
