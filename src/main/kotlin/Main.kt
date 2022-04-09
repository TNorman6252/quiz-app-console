import utils.ScannerInput

fun main(args: Array<String>) {

}

fun mainMenu() : Int {
    return ScannerInput.readNextInt("""
        > ----------------------------------
        > |        QUIZ APPLICATION        |
        > ----------------------------------
        > | QUIZ OPTIONS:                      |
        > |   1) Play Quiz Game               |
        > |   2) Add a Question                |
        > |   3) Delete a Question           |
        > |   4) Number of Questions in App           |
        > |   5) Update a Question
        > |   6) List Questions - (Sub-menu)
        > |   7) Save Question(s)
        > |   8) Load Question(s)
        > ----------------------------------
        > |   0) Exit                      |
        > ----------------------------------
        > ==>> """.trimMargin(">"))
}


