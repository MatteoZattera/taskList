import kotlinx.datetime.*

class Task(var content: String, var priority: String, var deadlineDateTime: String) {

    fun endDate() = deadlineDateTime.toLocalDateTime().date.toString()
    fun endHour() = deadlineDateTime.toLocalDateTime().hour.toString().padStart(2, '0')
    fun endMinute() = deadlineDateTime.toLocalDateTime().minute.toString().padStart(2, '0')

    fun priorityColor(): String {
        return when (priority) {
            "C" -> "\u001B[101m \u001B[0m"
            "H" -> "\u001B[103m \u001B[0m"
            "N" -> "\u001B[102m \u001B[0m"
            "L" -> "\u001B[104m \u001B[0m"
            else -> "X"
        }
    }

    fun expirationColor(): String {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
        val numberOfDays = currentDate.daysUntil(deadlineDateTime.toLocalDateTime().date)
        return when {
            numberOfDays < 0 -> "\u001B[101m \u001B[0m"
            numberOfDays > 0 -> "\u001B[102m \u001B[0m"
            else -> "\u001B[103m \u001B[0m"
        }
    }
}
