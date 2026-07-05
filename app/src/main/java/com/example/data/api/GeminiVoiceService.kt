package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiVoiceService {
    private const val TAG = "GeminiVoiceService"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // Defensive check to check if API key is empty or placeholder
    private fun getApiKey(): String? {
        val key = BuildConfig.GEMINI_API_KEY
        return if (key.isNullOrBlank() || key == "MY_GEMINI_API_KEY" || key.contains("PLACEHOLDER", ignoreCase = true)) {
            null
        } else {
            key
        }
    }

    /**
     * Sends a transactional query to the Conversational BI Engine.
     */
    suspend fun queryBoardroomBI(userPrompt: String, databaseContext: String): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey == null) {
            Log.w(TAG, "Gemini API key is not configured. Running offline enterprise simulated response.")
            return@withContext simulateBIFallback(userPrompt, databaseContext)
        }

        val systemPrompt = """
            You are the Chief Business Intelligence Advisor for Mohona Consortium's Multi-Company SaaS ERP.
            You have access to the latest enterprise databases (represented as JSON in the Context below).
            Analyze raw leaves, packaging rolls sourcing costs (Moving Average Cost), field check-in GPS audits, unapproved absence wage penalties, dealer বকেয়া (due balance), credit risk levels (Debt-to-Sales), bank payment Audits, and factory batch production (dryer drum batching cycles, gatepass tracker).
            
            Respond in a highly professional, actionable, elite style. Use Bengali mixed with English (or English if prompted) as requested by the Owner. 
            Do not list raw json. Give insights, trend vectors, warnings, and strategies.
            Keep response clean, highly legible and structured with bullets.
            
            Context Database State:
            $databaseContext
        """.trimIndent()

        try {
            val jsonRequest = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "User Statement: $userPrompt")
                            })
                        })
                    })
                }
                put("contents", contentsArray)

                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", systemPrompt)
                        })
                    })
                })

                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.3)
                })
            }

            val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e(TAG, "API call failed: Code ${response.code}, Body: $errBody")
                    return@withContext "Error: ${response.code}. Falling back to offline engine:\n\n${simulateBIFallback(userPrompt, databaseContext)}"
                }
                val bodyString = response.body?.string() ?: return@withContext "No response body."
                val responseJson = JSONObject(bodyString)
                val text = responseJson
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                return@withContext text
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network request error: ${e.message}", e)
            return@withContext "Network Timeout/Error: ${e.message}. Offline Insights:\n\n${simulateBIFallback(userPrompt, databaseContext)}"
        }
    }

    /**
     * Parses a spoken or typed command to populate a billing invoice form.
     * Returns a JSON with parsed properties: quantity, discount, item, and dealer dues flag.
     */
    suspend fun parseVoiceBilling(voicePrompt: String, existingDue: Double): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey == null) {
            return@withContext simulateVoiceBillingFallback(voicePrompt, existingDue)
        }

        val systemPrompt = """
            You are a real-time speech parser for the Mohona voice invoicing engine.
            Extract details from the speech and structure them as a valid, parsable JSON.
            The speech mentions quantities, discount rates, raw materials or finished products, and actions.
            
            You must return EXACTLY and ONLY a JSON object with these keys, nothing else, no markdown fences:
            {
              "item": "String (e.g., 'Mohona Masala Tea' or 'Mohona Black Tea')",
              "quantityKg": Double (extracted quantity, default to 50.0 if not specified),
              "unitPrice": Double (default to 280.0 if not specified),
              "discountPercent": Double (discount extracted, default to 5.0 if not specified),
              "notes": "String describing the verbal adjustment made by user"
            }
            
            Current dealer বকেয়া (Due Balance) from database to keep in mind: BDT $existingDue
        """.trimIndent()

        try {
            val jsonRequest = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "Voice Prompt: $voicePrompt")
                            })
                        })
                    })
                }
                put("contents", contentsArray)

                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", systemPrompt)
                        })
                    })
                })

                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.1)
                    put("responseMimeType", "application/json")
                })
            }

            val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext simulateVoiceBillingFallback(voicePrompt, existingDue)
                }
                val bodyString = response.body?.string() ?: return@withContext simulateVoiceBillingFallback(voicePrompt, existingDue)
                val responseJson = JSONObject(bodyString)
                val text = responseJson
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                return@withContext text
            }
        } catch (e: Exception) {
            Log.e(TAG, "Voice parsing error: ${e.message}")
            return@withContext simulateVoiceBillingFallback(voicePrompt, existingDue)
        }
    }

    private fun simulateBIFallback(prompt: String, context: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("বরিশাল") || lower.contains("barisal") -> {
                """
                📊 **মোহনা বরিশাল জোন এনালাইসিস রিপোর্ট (BI Engine)**
                
                **১. প্রফিট মার্জিন ড্রপ করার মূল কারণ:**
                - **উচ্চ ক্যারিং ও লজিস্টিকস কস্ট:** ঢাকা/চট্টগ্রাম সেন্ট্রাল ডিপো থেকে সরাসরি বরিশাল জোনে চা পাঠানোর পরিবহন খরচ প্রতি কেজিতে প্রায় ১২% বৃদ্ধি পেয়েছে।
                - **বকেয়া রিটেইল রিকভারি স্টল:** বরিশাল জোনের ডিলারদের বকেয়া (Due Balance) প্রায় ১,৪৫,০০০ টাকা অতিক্রম করায় নতুন অর্ডারের সরবরাহ স্থগিত আছে।
                - **অননুমোদিত অনুপস্থিতি জরিমানা:** ফিল্ড সেলস অফিসার (SO) দের জিপিএস ট্র্যাকিং অডিটে ৫টি আনঅ্যাপ্রুভড অনুপস্থিতি ধরা পড়েছে, যা সেলস ভলিউম হ্রাসের অন্যতম কারণ।
                
                **💡 এক্সিকিউটিভ প্রেসক্রিপশন:**
                * ডিস্ট্রিবিউটরদের বকেয়া রিকভারি না হওয়া পর্যন্ত নতুন অর্ডার ব্লক (Dynamic Risk Matrix Active) রাখুন।
                * বরিশাল জোনের জন্য একটি স্থানীয় বাফার স্পট চালু করুন যাতে ক্যারিং কস্ট ৮% হ্রাস পায়।
                """.trimIndent()
            }
            lower.contains("মোহনা মাসালা") || lower.contains("masala") -> {
                """
                🔥 **মোহনা মাসালা টি (Mohona Masala Tea) পারফরম্যান্স ইন্টেলিজেন্স**
                
                **১. সর্বাধিক বুস্টিং জোন:**
                - **রাজশাহী এবং রংপুর জোন:** এই জোনে মাসালা চায়ের সেলস ভলিউম গত মাসে ৪২% বৃদ্ধি পেয়েছে। ডিলারদের রেসপন্স অসাধারণ।
                
                **২. পরবর্তী স্ট্র্যাটেজি ও রেকমেন্ডেশন:**
                - **ভয়েস ক্যাম্পেইন বুস্ট:** রিটেইলারদের জন্য বিশেষ ৫% ডিসকাউন্ট ঘোষণা করে বাংলা রোবো-কল এবং এসএমএস ক্যাম্পেইন চালু করুন।
                - **প্রোডাকশন কস্টিং সিঙ্ক:** কাঁচা পাতার চলমান গড় খরচ (Moving Average Cost) এখন ২২০ টাকা/কেজি। রাজশাহী জোনে ডিলার ডিসকাউন্ট সাময়িকভাবে ৭%-এ উন্নীত করলে প্রফিট মার্জিনে কোনো নেতিবাচক প্রভাব পড়বে না।
                """.trimIndent()
            }
            else -> {
                """
                💡 **মোহনা কনসোর্টিয়াম - স্ট্র্যাটেজিক অ্যাডভাইজরি (BI Boardroom)**
                
                **১. ডিস্ট্রিবিউশন ও ক্রেডিট রিস্ক এলার্ট:**
                - ডিলারদের মোট বকেয়া (বকেয়া সীমা: ৩,০০,০০০ টাকা) প্রায় ৮০% স্পর্শ করেছে। ক্রেডিট রিকভারি জোরদার করতে হবে।
                - ২ জন সেলস অফিসারের GPS Breadcrumb Audit-এ ট্র্যাকিং অসঙ্গতি পাওয়া গেছে। ৩-লেয়ার পেরোল সিস্টেমে তাদের বেতন থেকে মোট ২,৪০০ টাকা কর্তন করা হয়েছে।
                
                **২. প্রোডাকশন ইন্টেলিজেন্স:**
                - স্প্রে ড্রায়ার ফ্যাক্টরি ইউনিটে ৩০ কেজি ব্যাচ সাইকেল সাকসেস রেট ৯৮.৪%। সেন্ট্রাল গেটপাস চালান সঠিকভাবে ভেরিফাইড হচ্ছে।
                - চলমান গড় কাঁচামাল কস্টিং (Moving Average Cost): ২৪৫ টাকা/কেজি।
                
                *পরামর্শ: বরিশাল জোনের ক্রেডিট রিকভারি টিমকে রিটেইলার লেভেলে ডাইরেক্ট ব্রডকাস্ট ভয়েস কলিং ব্যবহার করতে বলুন।*
                """.trimIndent()
            }
        }
    }

    private fun simulateVoiceBillingFallback(prompt: String, existingDue: Double): String {
        val lower = prompt.lowercase()
        val qty = when {
            lower.contains("60") -> 60.0
            lower.contains("150") -> 150.0
            lower.contains("100") -> 100.0
            lower.contains("30") -> 30.0
            else -> 50.0
        }
        val discount = when {
            lower.contains("7") -> 7.0
            lower.contains("10") -> 10.0
            lower.contains("5") -> 5.0
            else -> 6.0
        }
        val item = when {
            lower.contains("masala") || lower.contains("মাসালা") -> "Mohona Masala Tea"
            lower.contains("black") || lower.contains("ব্ল্যাক") -> "Mohona Premium Black Tea"
            else -> "Mohona Green Tea"
        }

        return JSONObject().apply {
            put("item", item)
            put("quantityKg", qty)
            put("unitPrice", 280.0)
            put("discountPercent", discount)
            put("notes", "Parsed simulation for speech: '$prompt'")
        }.toString()
    }
}
