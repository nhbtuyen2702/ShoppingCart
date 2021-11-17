package com.shoppingcart.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.Query;

public class PaginationResult<E> {//E = ProductInfo

	private int totalRecords;

	private int currentPage;

	private List<E> list; //List<ProductInfo> list = null;

	private int maxResult;

	private int totalPages;

	private int maxNavigationPage;

	private List<Integer> navigationPages;

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getMaxNavigationPage() {
		return maxNavigationPage;
	}

	public void setMaxNavigationPage(int maxNavigationPage) {
		this.maxNavigationPage = maxNavigationPage;
	}

	public List<Integer> getNavigationPages() {
		return navigationPages;
	}

	public void setNavigationPages(List<Integer> navigationPages) {
		this.navigationPages = navigationPages;
	}

	// @page: 1, 2, ..
	public PaginationResult(Query query, int page, int maxResult, int maxNavigationPage) {
		int pageIndex = page - 1 < 0 ? 0 : page - 1;//1 - 1 < 0 ? 0 : 1 - 1 -->0     //2 - 1 < 0 ? 0 : 2 - 1 -->1
		int fromRecordIndex = pageIndex * maxResult;//0 * 5 -->0 //1 * 5 -->5 //bắt đầu từ index nào
		int maxRecordIndex = fromRecordIndex + maxResult;//0 + 5 -->5 //5 + 5 -->10 //điều kiện dừng

		ScrollableResults resultScroll = query.scroll(ScrollMode.SCROLL_INSENSITIVE);

		List<E> results = new ArrayList<E>();//List<ProductInfo> results = new ArrayList<ProductInfo>();

		boolean hasResult = resultScroll.first();

		if (hasResult) {
			// Cuộn tới vị trí:
			hasResult = resultScroll.scroll(fromRecordIndex);//0 //5
			if (hasResult) {
				do {
					int rowNumber = resultScroll.getRowNumber();
					E record = (E) resultScroll.get(0);//ProductInfo record = (ProductInfo)resultScroll.get(0);
					results.add(record);//results.add(record);
				} while (resultScroll.next() //resultScroll.next() có nhiệm vụ là kiểm tra phần tử kế tiếp có tồn tại hay ko, nếu có sẽ trỏ tới phần tử kế tiếp
						&& resultScroll.getRowNumber() >= fromRecordIndex //10>=5
						&& resultScroll.getRowNumber() < maxRecordIndex); //10<10
			}
			/*
			for (int i = fromRecordIndex; i < maxRecordIndex; i++) {
				E record = (E) resultScroll.get(i);//ProductInfo record = (ProductInfo)resultScroll.get(0);
				results.add(record);//results.add(record);
			}
			*/

			// Chuyển tới bản ghi cuối
			resultScroll.last();
		}

		// Tổng số bản ghi.
		this.totalRecords = resultScroll.getRowNumber() + 1;//resultScroll.getRowNumber() = i
		this.currentPage = pageIndex + 1;
		this.list = results;
		this.maxResult = maxResult;

		this.totalPages = (this.totalRecords / this.maxResult) + 1; //60 / 5 = 12
		this.maxNavigationPage = maxNavigationPage;//10 

		if (maxNavigationPage < this.totalPages) {
			this.maxNavigationPage = this.totalPages;
		}

		this.calcNavigationPages();
	}

	private void calcNavigationPages() {

		this.navigationPages = new ArrayList<Integer>();

		int current = this.currentPage > this.totalPages ? this.totalPages : this.currentPage;

		//TODO -4 -3 -2 -1 1 2 3 4 5 6 -->current = 2
		int begin = current - this.maxNavigationPage / 2;
		int end = current + this.maxNavigationPage / 2;

		//this.totalPages = 20;
		//int begin = -3; //current = 8
		//int end = 17;
		
 		// Trang đầu tiên
		this.navigationPages.add(1);
		if (begin > 2) {
			// Dùng cho '...'
			this.navigationPages.add(-1);
		}

		for (int i = begin; i < end; i++) {
			if (i > 1 && i < this.totalPages) {
				this.navigationPages.add(i);
			}
		}

		if (end < this.totalPages - 2) {
			// Dùng cho '...'
			this.navigationPages.add(-1);
		}
		//TODO
		
		// Trang cuối cùng.
		this.navigationPages.add(this.totalPages);
	}
}
