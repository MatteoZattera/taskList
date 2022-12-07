import kotlinx.datetime.*

fun main() {

    val taskList = TaskList()
    taskList.retrieveData()
    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        when (readln().lowercase()) {
            "add" -> {
                val priority = setPriority()
                val deadlineDate = setDate()
                val deadlineDateTime = setDateTime(deadlineDate)
                val content = setContent()

                if (content.isBlank()) println("The task is blank") else taskList.addTask(content, priority, deadlineDateTime)
            }
            "print" -> taskList.printAllTasks()
            "edit" -> {
                if (taskList.printAllTasks()) {
                    var taskNumber: Int? = null
                    while (taskNumber !in 1..taskList.getNumberOfTasks()) {
                        println("Input the task number (1-${taskList.getNumberOfTasks()}):")
                        taskNumber = readln().toIntOrNull()
                        if (taskNumber !in 1..taskList.getNumberOfTasks()) println("Invalid task number")
                    }

                    var field: String
                    while (true) {
                        println("Input a field to edit (priority, date, time, task):")
                        field = readln()
                        if (field.matches("priority|date|time|task".toRegex())) break
                        println("Invalid field")
                    }

                    when (field) {
                        "priority" -> taskList.editTask(taskNumber!!, field, setPriority())
                        "date" -> taskList.editTask(taskNumber!!, field, setDate())
                        "time" -> taskList.editTask(taskNumber!!, field, setDateTime(taskList.getDateOfTask(taskNumber)))
                        "task" -> taskList.editTask(taskNumber!!, field, setContent())
                    }
                    println("The task is changed")
                }
            }
            "delete" -> {
                if (taskList.printAllTasks()) {
                    var taskNumber: Int? = null
                    while (taskNumber !in 1..taskList.getNumberOfTasks()) {
                        println("Input the task number (1-${taskList.getNumberOfTasks()}):")
                        taskNumber = readln().toIntOrNull()
                        if (taskNumber !in 1..taskList.getNumberOfTasks()) println("Invalid task number")
                    }
                    taskList.deleteTask(taskNumber!!)
                    println("The task is deleted")
                }
            }
            "end" -> break
            else -> println("The input action is invalid")
        }
    }
    taskList.saveData()
    println("Tasklist exiting!")
}

fun setPriority(): String {
    var taskPriority: String
    while (true) {
        println("Input the task priority (C, H, N, L):")
        taskPriority = readln().uppercase()
        if (taskPriority.matches("[CHNL]".toRegex())) break
    }
    return taskPriority
}

fun setDate(): LocalDate {
    var date: LocalDate
    while (true) {
        println("Input the date (yyyy-mm-dd):")
        try {
            val (year, month, day) = readln().split("-".toRegex(), 3).map { it.toInt() }
            date = LocalDate(year, month, day)
            break
        } catch (e: Exception) {
            println("The input date is invalid")
        }
    }
    return date
}

fun setDateTime(date: LocalDate): LocalDateTime {
    var deadlineDateTime: LocalDateTime
    while (true) {
        println("Input the time (hh:mm):")
        try {
            val (hour, minute) = readln().split(":".toRegex(), 2).map { it.toInt() }
            deadlineDateTime = LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, hour, minute)
            break
        } catch (e: Exception) {
            println("The input time is invalid")
        }
    }
    return deadlineDateTime
}

fun setContent(): String {
    var taskContent = ""
    println("Input a new task (enter a blank line to end):")
    while (true) {
        val input = readln().trim()
        if (input.isBlank()) break
        taskContent += "$input\n"
    }
    return taskContent.trimEnd()
}
