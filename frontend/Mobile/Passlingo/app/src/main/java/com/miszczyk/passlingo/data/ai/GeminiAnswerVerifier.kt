package com.miszczyk.passlingo.data.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class AnswerVerdict(
    val isCorrect: Boolean,
)

class GeminiAnswerVerifier(apiKey: String) {
    private val model = GenerativeModel(
        modelName = "gemini-3.6-flash",
        apiKey = apiKey,
        generationConfig = generationConfig {
            responseMimeType = "application/json"
            temperature = 0f
        }
    )

    suspend fun verify(
        front: String,
        expectedAnswer: String,
        userAnswer: String
    ): Result<Boolean> = runCatching {
        val prompt = """
            You are checking a flashcard answer for a language-learning app.
            
            Term/prompt: "$front"
            Expected (reference) answer: "$expectedAnswer"
            User's answer: $userAnswer
            
            Mark the user's answer as correct if it is semantically equivalent
            to the reference answer, even with minor differences in spelling,
            word order, capitalization, synonyms, or small typos.
            Mark it as incorrect if the meaning is different, incomplete, or wrong.
            
            Respond ONLY with JSON in this exact format, nothing else:
            {"isCorrect": true}
            or
            {"isCorrect": false}
        """.trimIndent()

        val response = model.generateContent(prompt)
        val json = response.text ?: error("Empty response from Gemini")

        Json.decodeFromString<AnswerVerdict>(json).isCorrect
    }
}