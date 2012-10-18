package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType

import javax.jws.WebMethod
import javax.jws.WebResult

class PageService {

    static expose = [EndpointType.JAX_WS]

    @WebResult(name = 'hello')
    @WebMethod(operationName = 'sayHello')
    String sayHello() {
        'hello'
    }

    @WebResult(name = 'chapters')
    @WebMethod(operationName = 'getSomeChapters')
    List<Chapter> getSomeChapters() {
        List<Chapter> chapters = []
        chapters << createChapter([name: "Chapter1"], false, false)
        chapters << createChapter([name: "Chapter2"], true, false)
        chapters << createChapter([name: "Chapter3"], true, true)
        chapters
    }

    @WebResult(name = 'words')
    @WebMethod(operationName = 'getSomeWords')
    List<Word> getSomeWords() {
        createWords()
    }

    @WebResult(name = 'page')
    @WebMethod(operationName = 'getMeSomePersistedPagesWithWords')
    List<Page> getMeSomePersistedPagesWithWords() {
        List<Page> pages = []
        pages << createPage([name: "saved page 1", number: 1], true).save(flush: true)
        pages << createPage([name: "saved page 2", number: 2], true).save(flush: true)
        pages << createPage([name: "saved page 3", number: 3], true).save(flush: true)
        pages
    }

    @WebResult(name = 'page')
    @WebMethod(operationName = 'getMeSomePagesWithWords')
    List<Page> getMeSomePagesWithWords() {
        List<Page> pages = []
        pages << createPage([name: "test1", number: 2], true)
        pages << createPage([name: "hihi", number: 8], true)
        pages << createPage([name: "hoho", number: 32], true)
        pages
    }

    @WebResult(name = 'page')
    @WebMethod(operationName = 'getMeSomePages')
    List<Page> getMeSomePages() {
        List<Page> pages = []
        pages << createPage([name: "Page 1, version should increment", number: 2], false)
        pages << createPage([name: "Page 2, version should increment", number: 8], false)
        pages << createPage([name: "Page 3, version should increment", number: 32], true)
        pages
    }

    private createChapter(Map params, Boolean addPages = false, Boolean addWords = false) {
        Chapter chapter = Chapter.findOrCreateWhere(params)
        Integer pageNumber = 0
        if(addPages && !chapter?.pages) {
            def page = Page.findOrCreateWhere(name: "Page 1, no version", number: pageNumber++)
            if(addWords) {
                page.words = createWords(null)
            }
            chapter.addToPages(page)
        }
        chapter
    }

    private createPage(Map params, Boolean addWords = false) {
        Page page = Page.findOrCreateWhere(params)
        if(addWords && !page?.words) {
            createWords(page)
        }
        page
    }

    private List<Word> createWords(Page page = null) {
        def words = []
        words << new Word(text: "i")
        words << new Word(text: "am")
        words << new Word(text: "the")
        words << new Word(text: "doctor")
        if(page && !page.words)
            words.each { page.addToWords(it) }
        words
    }
}
