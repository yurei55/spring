package com.springmvc.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.h2.command.dml.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.springmvc.domain.Book;
import com.springmvc.exception.BookIdException;


@Repository
public class BookRepositoryImpl implements BookRepository {
	private JdbcTemplate template;
	
//	private List<Book> listOfBooks = new ArrayList<>();
	
	
	public BookRepositoryImpl() {
//		Book book1 = new Book("ISBN1234", "C# 교과서", 30000);
//		book1.setAuthor("박용준");
//		book1.setDescription("C# 교과서』는 생애 첫 프로그래밍 언어로 C#을 시작하는 독자를 대상으로 한다. 특히 응용 프로그래머를 위한 C# 입문서로, C#을 사용하여 게임(유니티), 웹, 모바일, IoT 등을 개발할 때 필요한 C# 기초 문법을 익히고 기본기를 탄탄하게 다지는 것이 목적이다.");
//		book1.setPublisher("길벗");
//		book1.setCategory("IT전문서");
//		book1.setUnitsInStock(1000);
//		book1.setReleaseDate("2020/05/29");
//
//		Book book2 = new Book("ISBN1235", "Node.js 교과서", 36000);
//		book2.setAuthor("조현영");
//		book2.setDescription("이 책은 프런트부터 서버, 데이터베이스, 배포까지 아우르는 광범위한 내용을 다룬다. 군더더기 없는 직관적인 설명으로 기본 개념을 확실히 이해하고, 노드의 기능과 생태계를 사용해보면서 실제로 동작하는 서버를 만들어보자. 예제와 코드는 최신 문법을 사용했고 실무에 참고하거나 당장 적용할 수 있다.");
//		book2.setPublisher("길벗");
//		book2.setCategory("IT전문서");
//		book2.setUnitsInStock(1000);
//		book2.setReleaseDate("2020/07/25");
//
//		Book book3 = new Book("ISBN1236", "어도비 XD CC 2020", 25000);
//		book3.setAuthor("김두한");
//		book3.setDescription("어도비 XD 프로그램을 통해 UI/UX 디자인을 배우고자 하는 예비 디자이너의 눈높이에 맞게 기본적인 도구를 활용한 아이콘 디자인과 웹&앱 페이지 디자인, UI 디자인, 앱 디자인에 애니메이션과 인터랙션을 적용한 프로토타이핑을 학습합니다.");
//		book3.setPublisher("길벗");
//		book3.setCategory("IT활용서");
//		book3.setUnitsInStock(1000);
//		book3.setReleaseDate("2019/05/29");
//
//		this.listOfBooks.add(book1);
//		this.listOfBooks.add(book2);
//		this.listOfBooks.add(book3);
	}
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
	@Override
	public List<Book> getAllBookList() {
		String sql = "select b_bookId, b_name, b_unitPrice, b_author, b_description, b_publisher, b_category, b_unitsInStock, b_releaseDate, b_condition, b_fileName from book";
		List<Book> listOfBooks = this.template.query(sql, new BookRowMapper());
		
		return listOfBooks;
	}

	@Override
	public List<Book> getBookListByCategory(String category) {
		String sql = "select b_bookId, b_name, b_unitPrice, b_author, b_description, b_publisher, b_category, b_unitsInStock, b_releaseDate, b_condition, b_fileName from book"
				+ " where b_category like '%' || ? || '%' ";
		List<Book> listOfBooks = this.template.query(sql, new BookRowMapper(), category);

		return listOfBooks;
	}

	@Override
	public Set<Book> getBookListByFilter(Map<String, List<String>> filter) {
		Set<Book> booksByPublisher = new HashSet<Book>();
		Set<Book> booksByCategory = new HashSet<Book>();

		Set<String> booksByFilter = filter.keySet();

		if (booksByFilter.contains("publisher")) {
			for (int j = 0; j < filter.get("publisher").size(); j++) {
				String publisherName = filter.get("publisher").get(j);
				String SQL = "SELECT b_bookId, b_name, b_unitPrice, b_author, b_description, b_publisher, b_category, b_unitsInStock, b_releaseDate, b_condition, b_fileName FROM book where b_publisher LIKE '%' || ? || '%'";
				System.out.println("PUBLISHER: " + publisherName);
				booksByPublisher.addAll(this.template.query(SQL, new BookRowMapper(), publisherName));
				
				System.out.println("CATEGORY: " + booksByPublisher);
			}
		}  

		if (booksByFilter.contains("category")) {
			for (int i = 0; i < filter.get("category").size(); i++) {
				String category = filter.get("category").get(i);
				String SQL = "SELECT b_bookId, b_name, b_unitPrice, b_author, b_description, b_publisher, b_category, b_unitsInStock, b_releaseDate, b_condition, b_fileName FROM book where b_category LIKE '%' || ? || '%'";
				System.out.println("CATEGORY: " + category);
				booksByCategory.addAll(template.query(SQL, new BookRowMapper(), category));
				
				System.out.println("CATEGORY: " + booksByCategory);
			}
		}  

		booksByCategory.retainAll(booksByPublisher);
		
		System.out.println("CATEGORY: " + booksByCategory);

		return booksByCategory;
	}

	@Override
	public Book getBookById(String bookId) {
		Book bookInfo = null;

		String sql = "SELECT count(*) FROM book where b_bookId=?";
		int rowCount = this.template.queryForObject(sql, Integer.class, bookId);
		if(rowCount != 0) {
			sql = "SELECT b_bookId, b_name, b_unitPrice, b_author, b_description, b_publisher, b_category, b_unitsInStock, b_releaseDate, b_condition, b_fileName FROM book where b_bookId=?";
			bookInfo = this.template.queryForObject(sql, new BookRowMapper(), bookId);
		}
		
		if(bookInfo == null) {
			//throw new IllegalArgumentException("도서 ID가 " + bookId + "인 도서를 찾을 수 없습니다.");
			throw new BookIdException(bookid);
		}
		return bookInfo;
	}

	@Override
	public void setNewBook(Book book) {
		String sql = "INSERT INTO book (b_bookId, b_name, b_unitPrice, b_author, b_description, b_publisher, b_category, b_unitsInStock, b_releaseDate, b_condition, b_fileName) "
                   + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		this.template.update(sql, book.getBookId(), book.getName(), book.getUnitPrice(), book.getAuthor(),
				book.getDescription(), book.getPublisher(), book.getCategory(), book.getUnitsInStock(),
				book.getReleaseDate(), book.getCondition(), book.getFileName());
	}
	

//	@Override
//	public List<Book> getAllBookList() {
//		return this.listOfBooks;
//	}

}
