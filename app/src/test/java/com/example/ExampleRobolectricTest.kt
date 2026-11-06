package com.example

import com.example.data.StationSchedule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

    @Test
    fun testStationSchedulePrograms() {
        val currentShow = StationSchedule.getCurrentProgram()
        assertNotNull(currentShow)
        assertTrue(StationSchedule.programs.size >= 10)
        assertTrue(StationSchedule.programs.any { it.title.contains("Morning Drive", ignoreCase = true) })
        assertTrue(StationSchedule.programs.any { it.title.contains("Matters of State", ignoreCase = true) })
        assertTrue(StationSchedule.programs.any { it.title.contains("Sports Arena", ignoreCase = true) })
    }

    @Test
    fun testProgramTimeSlots() {
        StationSchedule.programs.forEach { program ->
            assertNotNull(program.title)
            assertNotNull(program.host)
            assertTrue(program.startHour in 0..23)
            assertTrue(program.endHour in 1..24)
            assertTrue(program.timeSlot.isNotEmpty())
        }
    }
}
