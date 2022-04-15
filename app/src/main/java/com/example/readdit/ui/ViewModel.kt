package com.example.readdit.ui

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.readdit.ui.article.ReadHistoryData
import com.example.readdit.ui.FirestoreRepository
import com.example.readdit.ui.article.ArticleData
import com.example.readdit.ui.explore.ExploreData
import com.example.readdit.ui.notes.NotesData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.*

class ViewModel : ViewModel() {
    var firebaseRepository = FirestoreRepository()

    private val _explore: MutableLiveData<ArrayList<ExploreData>> = MutableLiveData()
    val explore: LiveData<ArrayList<ExploreData>> get() = _explore

    private val _bookmarked: MutableLiveData<ArrayList<ReadHistoryData>> = MutableLiveData()
    val bookmarked: LiveData<ArrayList<ReadHistoryData>> get() = _bookmarked

    private val _readhistory: MutableLiveData<ArrayList<ReadHistoryData>> = MutableLiveData()
    val readhistory: LiveData<ArrayList<ReadHistoryData>> get() = _readhistory

    private val _history: MutableLiveData<ArrayList<ReadHistoryData>> = MutableLiveData()
    val history: LiveData<ArrayList<ReadHistoryData>> get() = _history

    private val _homehistory: MutableLiveData<ArrayList<ReadHistoryData>> = MutableLiveData()
    val homehistory: LiveData<ArrayList<ReadHistoryData>> get() = _homehistory

    private val _article: MutableLiveData<ArrayList<ArticleData>> = MutableLiveData()
    val article: LiveData<ArrayList<ArticleData>> get() = _article

    private val _filteredArticle: MutableLiveData<ArrayList<ArticleData>> = MutableLiveData()
    val filteredArticle: LiveData<ArrayList<ArticleData>> get() = _filteredArticle

    private val _quote: MutableLiveData<ArrayList<QuoteData>> = MutableLiveData()
    val quote: LiveData<ArrayList<QuoteData>> get() = _quote

    private val _notes: MutableLiveData<ArrayList<NotesData>> = MutableLiveData()
    val notes: LiveData<ArrayList<NotesData>> get() = _notes

    init {
        getExplore()
        getArticle()
        getReadHistory()
        getBookmarked()
        getHistory()
        getQuote()
        getHomeHistory()
        getNotes()
    }

    fun getExplore(){
        firebaseRepository.getExplore()
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.d("kfc", "Listen failed.", e)
                    return@EventListener
                }

                var exploreList: ArrayList<ExploreData> = arrayListOf()
                for (doc in value!!) {
                    var item = doc.toObject(ExploreData::class.java)
                    exploreList.add(item)
                }
                _explore.value = exploreList
            })
    }

    fun getReadHistory(){
        firebaseRepository.getReadHistory().whereEqualTo("user", "user001")
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.d("kfc", "Listen failed.", e)
                    return@EventListener
                }

                var readHistoryList: ArrayList<ReadHistoryData> = arrayListOf()
                for (doc in value!!) {
                    var item = doc.toObject(ReadHistoryData::class.java)
                    readHistoryList.add(item)
                }
                _readhistory.value = readHistoryList
            })
    }
    fun getHistory() {
        firebaseRepository.getReadHistory().whereEqualTo("user", "user001")
            .whereEqualTo("isRead", true).orderBy("readDate",Query.Direction.DESCENDING)
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.d("kfc", "Listen failed.", e)
                    return@EventListener
                }

                var historyList: ArrayList<ReadHistoryData> = arrayListOf()
                for (doc in value!!) {
                    var item = doc.toObject(ReadHistoryData::class.java)
                    historyList.add(item)
                }
                _history.value = historyList
            })
    }

    fun getArticle(){
        firebaseRepository.getArticle().orderBy("datePosted",Query.Direction.DESCENDING)
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.d("kfc", "Listen failed.", e)
                    return@EventListener
                }
                var articleList: ArrayList<ArticleData> = arrayListOf()
                for (doc in value!!) {
                    var item = doc.toObject(ArticleData::class.java)
                    articleList.add(item)
                }
                _article.value = articleList
            })
    }

    fun getNotes(){
        firebaseRepository.getNotes().orderBy("lastEdited",Query.Direction.DESCENDING)
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.d("kfc", "Listen failed.", e)
                    return@EventListener
                }
                var notesList: ArrayList<NotesData> = arrayListOf()
                for (doc in value!!) {
                    var item = doc.toObject(NotesData::class.java)
                    notesList.add(item)
                }
                _notes.value = notesList
            })
    }

    fun getFilteredList(readhisrtory:ArrayList<ReadHistoryData>,article:ArrayList<ArticleData>):ArrayList<ArticleData>{
        val list = arrayListOf<ArticleData>()
                for (i in readhisrtory) {
                    for (j in article) {
                        if (i.article == j.id) {
                            list.add(j)
                        }
                    }
                }
        return list
    }

    fun getFilteredArticle(topic: String){
        var articleList: ArrayList<ArticleData> = arrayListOf()
        firebaseRepository.getArticle().whereEqualTo("topic",topic)
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.d("kfc", "Listen failed.", e)
                    return@EventListener
                }
                for (doc in value!!) {
                    var item = doc.toObject(ArticleData::class.java)
                    articleList.add(item)
                }
                _filteredArticle.value = articleList
            })
    }

    fun getDetailArticle(articleID : String,callback:(ArticleData)-> Unit){
        var articleDetail : ArticleData
        firebaseRepository.getArticle().document(articleID).get().addOnSuccessListener {
            articleDetail =  it.toObject(ArticleData::class.java)!!
            callback(articleDetail)
        }
    }
    fun getBookmarked() {
        firebaseRepository.getReadHistory().whereEqualTo("user", "user001")
            .whereEqualTo("isBookmarked", true)
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.d("kfc", "Listen failed.", e)
                    return@EventListener
                }

                var bookmarkedList: ArrayList<ReadHistoryData> = arrayListOf()
                for (doc in value!!) {
                    var item = doc.toObject(ReadHistoryData::class.java)
                    bookmarkedList.add(item)
                }
                _bookmarked.value = bookmarkedList
            })

    }
    fun getHomeHistory(){
        firebaseRepository.getReadHistory().whereEqualTo("user", "user001")
            .whereEqualTo("isRead", true).orderBy("readDate",Query.Direction.DESCENDING).limit(2)
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.d("kfc", "Listen failed.", e)
                    return@EventListener
                }

                var historyList: ArrayList<ReadHistoryData> = arrayListOf()
                for (doc in value!!) {
                    var item = doc.toObject(ReadHistoryData::class.java)
                    historyList.add(item)
                }
                _homehistory.value = historyList
            })
    }

    fun getQuote(){
        firebaseRepository.getQuote()
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.d("kfc", "Listen failed.", e)
                    return@EventListener
                }

                var quoteList: ArrayList<QuoteData> = arrayListOf()
                for (doc in value!!) {
                    var item = doc.toObject(QuoteData::class.java)
                    quoteList.add(item)
                }
                _quote.value = quoteList
            })
    }

    fun checkReadHistory(readhistories: ArrayList<ReadHistoryData>, articleID: String): String? {
        for (readhistory in readhistories){
            if (readhistory.article == articleID){
                return readhistory.id
            }
        }
        return null
    }
    fun addReadHistory(articleID: String){
        val data = hashMapOf(
            "article" to articleID,
            "isBookmarked" to false,
            "isRead" to true,
            "readDate" to FieldValue.serverTimestamp(),
            "user" to "user001"
        )
        firebaseRepository.getReadHistory().add(data)
    }

    fun updateReadHistory(readHistoryID: String){
        firebaseRepository.getReadHistory().document(readHistoryID).update("readDate",FieldValue.serverTimestamp())
    }



    fun updateArticleReadCount(articleID: String,readCount: Int){
        firebaseRepository.getArticle().document(articleID).update("readCount",readCount+1)
    }

}