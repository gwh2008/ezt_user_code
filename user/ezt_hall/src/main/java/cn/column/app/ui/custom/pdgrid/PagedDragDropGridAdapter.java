package cn.column.app.ui.custom.pdgrid;


import android.view.View;

public interface PagedDragDropGridAdapter {

    // Automatic child distribution
	public final static int AUTOMATIC = -1; 
	
	// Delete drop zone location TOP
	public final static int TOP = 1;
	
	// Delete drop zone location BOTTOM
	public final static int BOTTOM = 2;
	
	/**
	 * Used to create the paging
	 * 
	 * @return the page count
	 */
	public int pageCount();

	/**
	 * Returns the count of item in a page
	 * 
	 * @param page index
	 * @return item count for page
	 */
	public int itemCountInPage(int page);
	
	/**
	 * Returns the view for the item in the page
	 * 
	 * @param page page
	 * @param index index
	 * @return the view 
	 */
	public View view(int page, int index);
	
	/**
	 * The fixed row count (AUTOMATIC for automatic computing)
	 * 
	 * @return row count or AUTOMATIC
	 */
	public int rowCount();
	
	/**
	 * The fixed column count (AUTOMATIC for automatic computing)
	 * 
	 * @return column count or AUTOMATIC
	 */
	public int columnCount();

	/**
	 * Prints the layout in Log.d();
	 */
	public void printLayout();

	/**
	 * Swaps two items in the item list in a page
	 * 
	 * @param pageIndex
	 * @param itemIndexA
	 * @param itemIndexB
	 */
	public void swapItems(int pageIndex, int itemIndexA, int itemIndexB);

	/**
	 * Moves an item in the page on the left of provided the page
	 * 
	 * @param pageIndex
	 * @param itemIndex
	 */
	public void moveItemToPreviousPage(int pageIndex, int itemIndex);

	/**
	 * Moves an item in the page on the right of provided the page
	 * 
	 * @param pageIndex
	 * @param itemIndex
	 */
	public void moveItemToNextPage(int pageIndex, int itemIndex);

	
	/**
	 * deletes the item in page and at position
	 * 
	 * @param pageIndex
	 * @param itemIndex
	 */
	public void deleteItem(int pageIndex, int itemIndex);

	/** 
	 * Returns the delete drop zone location.  
	 * 
	 * @return TOP or BOTTOM. 
	 */
    public int deleteDropZoneLocation();

    /**
     * Tells the grid to show or not the remove drop zone when moving an item
     */
    public boolean showRemoveDropZone();
}
