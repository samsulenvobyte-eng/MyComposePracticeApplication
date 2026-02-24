package com.example.mypracticeapplication.presentation.scrape

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * ScrapeRepository.kt - Data Layer for Web Scraping
 *
 * This repository handles all network operations and HTML parsing.
 * It follows the Repository pattern to abstract data source details from the ViewModel.
 *
 * LEARNING POINTS:
 * 1. Repository pattern separates data access from business logic
 * 2. All network calls MUST run on Dispatchers.IO (background thread)
 * 3. OkHttp is used for making HTTP requests
 * 4. Jsoup is used for parsing HTML and extracting data using CSS selectors
 */
class ScrapeRepository {

    /**
     * OkHttp client configured with reasonable timeouts.
     *
     * BEST PRACTICE: Always set timeouts to prevent hanging requests.
     * - connectTimeout: Time to establish connection
     * - readTimeout: Time to wait for response data
     * - writeTimeout: Time to send request data
     */
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Fetches sentence examples for a given word from sentencedict.com
     *
     * @param word The English word to search for sentence examples
     * @return Result containing list of sentences on success, or exception on failure
     *
     * LEARNING POINTS:
     * 1. withContext(Dispatchers.IO) switches to background thread for network calls
     * 2. Result type provides a safe way to handle success/failure without exceptions
     * 3. The URL is constructed by appending the word to the base URL
     */
    suspend fun fetchSentences(word: String): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            // Step 1: Validate input
            val cleanWord = word.trim().lowercase()
            if (cleanWord.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("Please enter a word to search"))
            }

            // Step 2: Construct the URL
            // sentencedict.com format: https://sentencedict.com/word.html
            val url = "https://sentencedict.com/$cleanWord.html"

            // Step 3: Build the HTTP request
            // BEST PRACTICE: Set a User-Agent header to identify your app
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Android; Learning App)")
                .get()
                .build()

            // Step 4: Execute the request and get response
            val response = client.newCall(request).execute()

            // Step 5: Check if request was successful (HTTP 200-299)
            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    IOException("Word not found or server error (HTTP ${response.code})")
                )
            }

            // Step 6: Get the response body (HTML content)
            val html = response.body?.string()
                ?: return@withContext Result.failure(IOException("Empty response from server"))

            // Step 7: Parse HTML and extract sentences
            val sentences = parseSentencesFromHtml(html)

            if (sentences.isEmpty()) {
                return@withContext Result.failure(
                    NoSuchElementException("No sentences found for '$cleanWord'")
                )
            }

            Result.success(sentences)

        } catch (e: IOException) {
            // Network errors (no internet, timeout, etc.)
            Result.failure(IOException("Network error: ${e.message ?: "Check your internet connection"}"))
        } catch (e: Exception) {
            // Unexpected errors
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }

    /**
     * Parses HTML content to extract sentence examples using Jsoup.
     *
     * @param html The raw HTML content from the website
     * @return List of extracted sentences
     *
     * CSS SELECTOR EXAMPLE:
     * The selector "div#all > div" targets:
     * - div elements that are direct children of a div with id="all"
     *
     * LEARNING POINTS:
     * 1. Jsoup.parse() converts HTML string to a Document object
     * 2. select() uses CSS selectors (like jQuery) to find elements
     * 3. text() extracts the text content from an element
     * 4. Always inspect the target website's HTML structure to build selectors
     */
    private fun parseSentencesFromHtml(html: String): List<String> {
        // Parse the HTML string into a Document
        val document = Jsoup.parse(html)

        // CSS Selector explanation:
        // "div#all > div" means:
        // - Find a div with id="all"
        // - Get its direct child div elements
        // These divs contain the sentence examples on sentencedict.com
        val sentenceElements = document.select("div#all > div")

        // Extract text from each element and filter out empty strings
        return sentenceElements
            .map { element -> element.text().trim() }
            .filter { text -> text.isNotEmpty() }
            .take(20) // Limit to 20 sentences for better UX
    }
}


