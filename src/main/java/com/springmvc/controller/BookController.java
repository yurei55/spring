package com.springmvc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springmvc.domain.Book;
import com.springmvc.exception.BookIdException;
import com.springmvc.exception.CategoryException;
import com.springmvc.service.BookService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/books")
public class BookController {
	@Autowired
	private BookService bookService;

	@Value("${uploadPath}")
	private String uploadPath;

//	@RequestMapping
	@GetMapping
	public String requestBookList(Model model) {
		List<Book> list = this.bookService.getAllBookList();
		model.addAttribute("bookList", list);

		return "books";
	}

//	@RequestMapping("/all")
//	public String requestAllBooks(Model model) {
//		List<Book> list = this.bookService.getAllBookList();
//		model.addAttribute("bookList", list);
//		
//		return "books";
//	}

//	@GetMapping("/all")
//	public String requestAllBooks(Model model) {
//		List<Book> list = this.bookService.getAllBookList();
//		model.addAttribute("bookList", list);
//		
//		return "books";
//	}

	@GetMapping("/all")
	public ModelAndView requestAllBooks() {
		List<Book> list = this.bookService.getAllBookList();

		ModelAndView mav = new ModelAndView();
		mav.addObject("bookList", list);
		mav.setViewName("books");

		return mav;
	}

	@GetMapping("/{category}")
	public String requestBooksByCategory(@PathVariable("category") String bookCategory, Model model) {
		List<Book> booksByCategory = this.bookService.getBookListByCategory(bookCategory);

		if (booksByCategory == null || booksByCategory.isEmpty()) {
			throw new CategoryException();
		}

		model.addAttribute("bookList", booksByCategory);

		return "books";
	}

	@GetMapping("/filter/{bookFilter}")
	public String requestBooksByFilter(@MatrixVariable(pathVar = "bookFilter") Map<String, List<String>> bookFilter,
			Model model) {
		Set<Book> booksByFilter = this.bookService.getBookListByFilter(bookFilter);
		model.addAttribute("bookList", booksByFilter);

		return "books";
	}

	@GetMapping("/book")
	public String requestBookById(@RequestParam("id") String bookId, Model model) {
		Book bookById = this.bookService.getBookById(bookId);
		model.addAttribute("book", bookById);

		return "book";
	}

//	@GetMapping("/add")
//	public String requestAddBookForm(Book book) {
//		return "addBook";
//	}

	@GetMapping("/add")
	public String requestAddBookForm(@ModelAttribute("NewBook") Book book) {
		return "addBook";
	}

	@PostMapping("/add")
	public String submitAddNewBook(@ModelAttribute("NewBook") Book book) {
		MultipartFile bookImage = book.getBookImage();

		if (bookImage != null && !bookImage.isEmpty()) {
			String saveName = bookImage.getOriginalFilename();
			File saveFile = new File(this.uploadPath, saveName);
			try {
				bookImage.transferTo(saveFile);
				book.setFileName(saveName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.bookService.setNewBook(book);
		return "redirect:/books";
	}

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("addTitle", "신규 도서 등록");
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("bookId", "name", "unitPrice", "author", "description", "publisher", "category",
				"unitsInStock", "releaseDate", "condition", "bookImage");
	}

	@ExceptionHandler(value = { BookIdException.class })
	public ModelAndView handlerException(HttpServletRequest req, BookIdException exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("invalidBookId", exception.getBookId());
		mav.addObject("exception", exception);
		mav.addObject("url", req.getRequestURL() + "?" + req.getQueryString());
		mav.setViewName("errorBook");
		return mav;
	}

//	@GetMapping("/cars/{car}")
//	public String requestMethod(@PathVariable("car") String car,  @MatrixVariable(pathVar="car", name="color") String color, Model model) {
//		
//		System.out.println("CARS: " + car);
//		System.out.println("COLOR: " + color);
//		
//		
//		return "cars";
//	}

}
