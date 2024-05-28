package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.habitscreen.Habit
import com.example.myapplication.habitscreen.HabitDao
import com.example.myapplication.habitscreen.HabitEvent
import com.example.myapplication.habitscreen.HabitViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
class HabitViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private lateinit var viewModel: HabitViewModel
    private lateinit var habitDao: HabitDao

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        habitDao = mock()
        `when`(habitDao.getHabitsOrderedByName()).thenReturn(flowOf(emptyList()))

        viewModel = HabitViewModel(habitDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `set name updates state with new name`() = testScope.runBlockingTest {
        viewModel.onEventForHabit(HabitEvent.SetName("Exercise"))

        val state = viewModel.state.value
        assert(state.name == "Exercise")
    }

    @Test
    fun `set frequency updates state with new frequency`() = testScope.runBlockingTest {
        viewModel.onEventForHabit(HabitEvent.SetFrequency(Habit.Frequency.Daily))

        val state = viewModel.state.value
        assert(state.frequency == Habit.Frequency.Daily)
    }

    @Test
    fun `save habit calls upsertHabit on dao`() = testScope.runBlockingTest {
        val habit = Habit(name = "Exercise", frequency = Habit.Frequency.Daily, streak = 0, id = 0)

        viewModel.onEventForHabit(HabitEvent.SaveHabit(habit))

        verify(habitDao, times(1)).upsertHabit(habit)
    }

    @Test
    fun `delete habit calls deleteHabit on dao`() = testScope.runBlockingTest {
        val habit = Habit(name = "Exercise", frequency = Habit.Frequency.Daily, streak = 0, id = 0)

        viewModel.onEventForHabit(HabitEvent.DeleteHabit(habit))

        verify(habitDao, times(1)).deleteHabit(habit)
    }

    @Test
    fun `increment streak updates habit streak and calls upsertHabit on dao`() = testScope.runBlockingTest {
        val habit = Habit(name = "Exercise", frequency = Habit.Frequency.Daily, streak = 0, id = 0)

        viewModel.onEventForHabit(HabitEvent.IncrementStreak(habit))

        verify(habitDao).upsertHabit(any())
    }

    @Test
    fun `show dialog sets isAddingHabit to true`() = testScope.runBlockingTest {
        viewModel.onEventForHabit(HabitEvent.ShowDialog)

        val state = viewModel.state.value
        assert(state.isAddingHabit)
    }

    @Test
    fun `hide dialog sets isAddingHabit to false`() = testScope.runBlockingTest {
        viewModel.onEventForHabit(HabitEvent.HideDialog)

        val state = viewModel.state.value
        assert(!state.isAddingHabit)
    }

    @Test
    fun `reset streak updates habit streak to zero and calls upsertHabit on dao`() = testScope.runBlockingTest {
        val habit = Habit(name = "Exercise", frequency = Habit.Frequency.Daily, streak = 5, id = 0)

        viewModel.onEventForHabit(HabitEvent.ResetStreak(habit))

        verify(habitDao).upsertHabit(habit.copy(streak = 0))
    }
}