package com.springmvc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.domain.Book;
import com.springmvc.repository.BookRepository;


@Service
public class BookServiceImpl implements BookService {
	@Autowired
	private BookRepository bookRepository;

	
	@Override
	public List<Book> getAllBookList() {
		return this.bookRepository.getAllBookList();
	}


	@Override
	public List<Book> getBookListByCategory(String category) {
		List<Book> listOfBooks = this.bookRepository.getBookListByCategory(category);

		return listOfBooks;
	}


	@Override
	public Set<Book> getBookListByFilter(Map<String, List<String>> filter) {
		Set<Book> booksByFilter = this.bookRepository.getBookListByFilter(filter);

		return booksByFilter;
	}


	@Override
	public Book getBookById(String bookId) {
		Book book = this.bookRepository.getBookById(bookId);

		return book;
	}


	@Override
	public void setNewBook(Book book) {
		this.bookRepository.setNewBook(book);
	}

	
}
