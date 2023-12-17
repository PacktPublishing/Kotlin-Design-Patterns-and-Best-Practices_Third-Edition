import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

fun main() {
    val originalFlow = flowOf(1, 2, 3, 4, 5)

    val sharedFlow = originalFlow.shareIn(scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.Lazily,
        replay = 2)

    val stateFlow = originalFlow.stateIn(scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.Lazily,
        initialValue = 0)
}