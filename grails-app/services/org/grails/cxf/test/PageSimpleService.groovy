package org.grails.cxf.test

import org.grails.cxf.utils.EndpointType

class PageSimpleService {

    static expose = ['cxf']

    String sayHello() {
        'hello'
    }

    def getSomeChapters() {
        List<Chapter> chapters = []
        chapters << createPage([name: "Chapter1"], true)
        chapters << createPage([name: "Chapter2"], true)
        chapters << createPage([name: "Chapter3"], true)
        chapters
    }

    def getSomeWords() {
        createWords()
    }

    def getMeSomePagesWithWords() {
        List<Page> pages = []
        pages << createPage([name: "test1", number: 2, version: 1], true)
        pages << createPage([name: "hihi", number: 8, version: 1], true)
        pages << createPage([name: "hoho", number: 32, version: 1], true)
        pages
    }

    def getMeSomePages() {
        List<Page> pages = []
        pages << createPage([name: "test1", number: 2, version: 1], false)
        pages << createPage([name: "hihi", number: 8, version: 1], false)
        pages << createPage([name: "hoho", number: 32, version: 1], false)
        pages
    }

    private createChapter(Map params, Boolean addPages = false, addWords = false) {
        Chapter chapter = new Chapter(params)
        if(addPages) {
            chapter.addToPages(createPage([name: "test1", number: 2, version: 1], addWords))
        }
        chapter.save(flush: true)
    }

    private createPage(Map params, Boolean addWords = false) {
        Page page = new Page(params)
        if(addWords) {
            createWords().each {
                page.addToWords(it)
            }
        }
        page.save(flush: true)
    }

    private List<Word> createWords() {
        def words = []
        words << new Word(text: "i")
        words << new Word(text: "am")
        words << new Word(text: "the")
        words << new Word(text: "doctor")
        words
    }
}
