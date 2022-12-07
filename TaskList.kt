import kotlinx.datetime.*
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

class TaskList {

    private var list = mutableListOf<Task>()

    /** Adds a task to the task list */
    fun addTask(content: String, priority: String, deadlineDateTime: LocalDateTime) {
        list.add(Task(content.trim(' '), priority, deadlineDateTime.toString()))
    }

    /** Removes task at a specified position in the task list */
    fun deleteTask(id: Int) {
        list.removeAt(id - 1)
    }

    /** Edits a task field with the specified value */
    fun editTask(id: Int, field: String, value: Any) {
        when (field) {
            "priority" -> list[id - 1].priority = value as String
            "date" -> if (value is LocalDate) list[id - 1].deadlineDateTime =
                LocalDateTime(
                    value.year,
                    value.monthNumber,
                    value.dayOfMonth,
                    list[id - 1].deadlineDateTime.toLocalDateTime().hour,
                    list[id - 1].deadlineDateTime.toLocalDateTime().minute
                ).toString()
            "time" -> list[id - 1].deadlineDateTime = value.toString()
            "task" -> list[id - 1].content = value as String
        }
    }

    /** Prints all tasks and returns false if there is no tasks */
    fun printAllTasks(): Boolean {
        if (list.isEmpty()) {
            println("No tasks have been input")
            return false
        }

        println("+----+------------+-------+---+---+--------------------------------------------+")
        println("| N  |    Date    | Time  | P | D |                   Task                     |")
        println("+----+------------+-------+---+---+--------------------------------------------+")

        for (i in list.indices) {
            val chunkedContent = list[i].content.split("\n").map { it.chunked(44) }.flatten()
            val id = (i + 1).toString().padEnd(2, ' ')
            val date = list[i].endDate()
            val time = "${list[i].endHour()}:${list[i].endMinute()}"
            val priorityColor = list[i].priorityColor()
            val expirationColor = list[i].expirationColor()

            println("| $id | $date | $time | $priorityColor | $expirationColor |${chunkedContent[0].padEnd(44, ' ')}|")
            for (j in 1 until chunkedContent.size) {
                println("|    |            |       |   |   |${chunkedContent[j].padEnd(44, ' ')}|")
            }
            println("+----+------------+-------+---+---+--------------------------------------------+")
        }
        return true
    }

    /** Returns the number of tasks in the task list */
    fun getNumberOfTasks() = list.size

    /** Returns the date of the specified task */
    fun getDateOfTask(id: Int) = list[id - 1].deadlineDateTime.toLocalDateTime().date

    /** Retrieve data from the file "tasklist.json" if exists */
    fun retrieveData() {
        val jsonFile = File("tasklist.json")

        if(jsonFile.exists()) {
            val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val type = Types.newParameterizedType(List::class.java, Task::class.java)
            val ciaoAdapter = moshi.adapter<List<Task>>(type)
            list = (ciaoAdapter.fromJson(jsonFile.readText()) ?: emptyList())!!.toMutableList()
        }
    }

    /** Save current data in the "tasklist.json" file */
    fun saveData() {
        val jsonFile = File("tasklist.json")

        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, Task::class.java)
        val taskListAdapter = moshi.adapter<List<Task>>(type)
        val json: String = taskListAdapter.toJson(list)
        jsonFile.writeText(json)
    }
}
