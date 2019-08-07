package common

import org.hyperskill.hstest.v5.stage.BaseStageTest
import org.hyperskill.hstest.v5.testcase.CheckResult
import org.hyperskill.hstest.v5.testcase.TestCase
import flashcards.Main


abstract class FlashcardsStage7Test : BaseStageTest<FlashcardsStage567Clue>(Main::class.java) {

    override fun generate(): List<TestCase<FlashcardsStage567Clue>> {
        return createStage7Tests() + createStage6Tests() + createStage5Tests() + generateOldTests()
    }

    /**
     * This stage is auto-graded.
     *
     * The grader skips empty lines.
     *
     * Every action must start with words "input the action".
     *
     * The behaviour of "ask" action is the same as the behaviour of the previous stage.
     *
     * Please check the console example.
     */
    override fun check(reply: String, clue: FlashcardsStage567Clue): CheckResult {
        return checkStage567(reply, clue)
    }

    companion object {

        fun createStage5Tests(): List<TestCase<FlashcardsStage567Clue>> {
            // TODO: how to remove files? Now at least rewrite at the first test:

            return listOf(
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Export("capitals.txt"),
                                    Export("capitalsNew.txt"),
                                    Exit()
                            ),
                            true
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Add("Great Britain", "London"),
                                    Remove("Wakanda"),
                                    Import("capitals.txt", 0),
                                    Ask(listOf("London")),
                                    Export("capitalsNew.txt"),
                                    Exit()
                            ),
                            true
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Import("capitalsNew.txt", 1),
                                    Exit()
                            ),
                            true
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Import("capitalsNew.txt", 1),
                                    Add("France", "Paris"),
                                    Add("Russia", "Moscow"),
                                    Export("capitalsNew.txt"),
                                    Import("capitalsNew.txt", 3),
                                    Exit()
                            ),
                            true
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Import("capitalsNew.txt", 3),
                                    Add("Japan", "Tokyo"),
                                    Export("capitalsNew.txt"),
                                    Import("capitalsNew.txt", 4),
                                    Exit()
                            ),
                            false
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Add("Great Britain", "London"),
                                    Add("France", "Paris"),
                                    Add("Russia", "Moscow"),
                                    Add("Japan", "Tokyo"),
                                    Add("London", "Big Ben"),
                                    Remove("London"),
                                    Export("capitalsNew.txt"),
                                    Import("capitalsNew.txt", 4),
                                    Exit()
                            ),
                            true
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Add("Great Britain", "London"),
                                    Add("France", "Paris"),
                                    Add("Russia", "Moscow"),
                                    Add("Japan", "Tokyo"),
                                    Add("London", "Big Ben"),
                                    Remove("London"),
                                    Remove("Russia"),
                                    Remove("Japan"),
                                    Export("capitalsNew.txt"),
                                    Import("capitalsNew.txt", 2),
                                    Exit()
                            ),
                            false
                    )
            )
        }

        fun createStage6Tests(): List<TestCase<FlashcardsStage567Clue>> {
            // TODO: how to remove files? Now at least rewrite at the first test:

            return listOf(
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Log("lastLog.txt"),
                                    Exit()
                            ),
                            true
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Add("Great Britain", "London"),
                                    Ask(listOf("?")),
                                    HardestCard(),
                                    ResetStats(),
                                    Log("lastLog.txt"),
                                    Exit()
                            ),
                            true
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Add("Great Britain", "London"),
                                    Add("France", "Paris"),
                                    Add("Russia", "Moscow"),
                                    Ask(listOf("?", "?")),
                                    HardestCard(),
                                    Log("lastLog.txt"),
                                    ResetStats(),
                                    Exit()
                            ),
                            false
                    )
            )
        }

        fun createStage7Tests(): List<TestCase<FlashcardsStage567Clue>> {
            // TODO: how to remove files? Now at least rewrite at the first test:

            return listOf(
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Export("capitalsNew.txt"),
                                    Exit()
                            ),
                            true
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Add("Great Britain", "London"),
                                    Exit()
                            ),
                            true,
                            "-export", "capitalsNew.txt"
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Import("capitalsNew.txt", 1),
                                    Add("Russia", "Moscow"),
                                    Exit()
                            ),
                            true,
                            "-export", "capitalsNew.txt"
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Add("France", "Paris"),
                                    Export("capitalsNew.txt"),
                                    Exit()
                            ),
                            true,
                            "-import", "capitalsNew.txt"
                    ),
                    createFlashcardsStage567TestCase(
                            listOf(
                                    Import("capitalsNew.txt", 3),
                                    Exit()
                            ),
                            true
                    )
            )
        }
    }
}

fun revealRawTest(consoleInput: String, reply: String): String {
    return "Input:\n$consoleInput\nYour output:\n$reply\n\n"
}

fun revealTestWithoutActions(
        questions: List<String>,
        answers: List<String>,
        grades: List<String>
): String {
    val count = minOf(questions.size, answers.size, grades.size)

    return "Questions:\n\n" +
            (0 until count).joinToString("\n") {
                "Question:\n${questions[it]}\n" +
                        "Answer:\n${answers[it]}\n" +
                        "Grade:\n${grades[it]}\n"
            } +
            "\n"
}

