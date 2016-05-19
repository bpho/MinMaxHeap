public class MinMaxHeap {
	
	private int currentSize;
	private int[] arr;
	
	public MinMaxHeap(int capacity){//Constructor
		
		arr = new int[capacity + 1];
		currentSize = 0;
	
	}
	
//	public void printHeap(){
//		
//		for (int i = 1; i <= currentSize; i++)
//		{
//			System.out.println(arr[i]);
//		}
//		
//	}
	
	public boolean isFull(){return currentSize == arr.length - 1;}
	
	public boolean isEmpty(){return currentSize == 0;}
	
	// COMPLETE THE FOLLOWING METHODS
	public void insert(int x){//PRE: The heap is not full
		
		if (currentSize == arr.length - 1){			//ex: currentSize = 7 (3 full levels), after enlarge = 15 (add another empty level)
			enlargeArray(arr.length * 2 +1);     	//expands the size of the heap by adding another level
		}											//allows the possibility to add child nodes to another level
		
		int hole = ++currentSize;					//creates a hole for the new node to be inserted at the currentSize + 1;
		arr[hole] = x;								//place the int element in the hold
		percolateUp(hole);							//percolateUp to reestablish the min-max heap property, hole is basically an index	
	}
	
	public int min(){//PRE: The heap is not empty
		return arr[1];		//returns root which is always the smallest
	}
	
	public int max(){//PRE: The heap is not empty
		
		if (currentSize >= 3)
		{
			return Math.max(arr[2], arr[3]);	//finds max between the two children of the root
		}
		else if (currentSize == 2)
		{
			return arr[2];
		}
		else
		{
			return arr[1];
		}
	}
	
	public int deleteMin(){//PRE: The heap is not empty
		
		int minItem = min();			//retrieves min value
		arr[1] = arr[currentSize--];	//takes the node from the last element (then decrements for next use)
		percolateDownMin(1);			//percolates down to maintain the min-max heap, working with a new currentsize
		return minItem;					//returns minItem
		
	}
	
	public int deleteMax(){//PRE: The heap is not empty
		
		int maxItem = max();
		
		//method that checks which element in arr was max
		int maxPos = greaterMaxValue(2, 3);						//position of max value either at index 2 or 3
		arr[maxPos] = arr[currentSize--];						//that position is a hole filled with the last node
		percolateDownMax(maxPos);
		return maxItem;
	
	}
	
	// Private methods go here
	
	public void enlargeArray(int newSize){		//enlarges array/heap to fit another level of nodes
		int[] oldarr = arr;
		arr = new int[newSize];
		for (int i = 0; i < oldarr.length; i++)
		{
			arr[i] = oldarr[i];
		}
	}
	
	public void swap(int index1, int index2)		//swaps two elements in the array/on the heap
	{
		int temp = arr[index1];
		arr[index1] = arr[index2];
		arr[index2] = temp;
	}

	public boolean isMinLevel(int index)
	{
		if (index == 1)
		{
			return false;
		}
		else
		{
			int nodeLevel = (int) (Math.log(index)/Math.log(2));    //since log base 2 is used to find depth of node at index i
			if (nodeLevel % 2 == 0)											//Math.log(index)/Math.log(2) is the only trivial way to do it
			{
				return true;			//returns true if the remainder is 0 when dividing by 2, meaning it's even, meaning its on a min level
			}
			else
			{
				return false;			//index is currently on a max level/odd
			}
		}
	}
	
	public int parentIndex(int index)
	{
		if (index != 1)				//FORGOT TO CHECK IF PARENT WAS THE ROOT, BECAUSE IT WILL CHECK 0
		{							//NEVER MADE A IF PARENT EXISTS>>>>
			return index/2;
		}
		else
		{
			return index;
		}
		
	}
	
	public int grandparentIndex(int index)
	{

		return (parentIndex(index))/2;		//returns gparent

	}
	
	public boolean grandparentExists(int index)
	{
		if (grandparentIndex(index) < 1)	//if the index of the node is less than 1, which isn't possible, no grandparent exists
		{
			return false;				
		}
		else
		{
			return true;
		}
	}
	
	public void percolateUp(int index)
	{
		if (isMinLevel(index) == true)	//if the new index is inserted on a min level/even level
		{
			if (arr[index] > arr[parentIndex(index)])	//if index was on min level, it has to be less than the parent 
			{
				swap(index, parentIndex(index));	//if it's greater, a swap is necessary
				percolateUpMax(parentIndex(index));			//then the arr[index] switches to the parent index/position which is on a max level, so it continues percolating up
			}
			else
			{
				percolateUpMin(index);					//if the arr[index] is less than or equal to the parent, it's where it should be, and should percolateUp as a min
			}
		}
		else //if the index is inserted on a max level
		{
			if (arr[index] < arr[parentIndex(index)])	//same thing, just on a max level instead, where the arr[index] is supposed to be greater than the parent this time
			{
				swap(index, parentIndex(index));
				percolateUpMin(parentIndex(index));
			}
			else
			{
				percolateUpMax(index);
			}
		}
	}
	
	public void percolateUpMin(int index)
	{
		if (grandparentExists(index) == true)			//percolation only necessary if a grandparent exists
		{
			if (arr[index] < arr[grandparentIndex(index)])	//if index was on a min level, it should be larger than grandparent
			{
				swap(index, grandparentIndex(index)); 	//if not it is swapped
				percolateUpMin(grandparentIndex(index));			//recursively check from the grandparents position, which is also a min/even level
			}
		}	
	}
	
	public void percolateUpMax(int index)
	{
		if (grandparentExists(index) == true)		//similar to percolateUpMin, the index this time, since it's on a max level, should be less than the grandparent
		{
			if (arr[index] > arr[grandparentIndex(index)])
			{
				swap(index, grandparentIndex(index));
				percolateUpMax(grandparentIndex(index));
			}
		}
	}
	
	public int greaterMaxValue(int index1, int index2)
	{
		if (currentSize == 2)
		{
			return 2;
		}
		else if (currentSize == 1)
		{
			return 1;
		}
		else
		{
			if (arr[index1] > arr[index2])
			{
				return index1;
			}
			else
			{
				return index2;
			}
		}
	}
	
	public boolean hasChildren(int index)
	{
		if ((2*index) <= currentSize)			//index has at least one child that is still within/less than or equal to total amount of nodes
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean hasGrandChildren(int index)
	{
		if ((2*index) <= currentSize)			//same, but checking for possible grandchildren now
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public int findSmallestChild(int index)
	{
		if (index*2+1 > currentSize)						//if the second child exists outside of the currentSize, it takes in the first child
		{													//every empty node is 0 anyway so it would be returned but it's invalid
			return index*2;
		}
		else
		{
			if (arr[index*2] > arr[index*2+1])				//if the second child does exist and is less than first, return it
			{
				return index*2+1;
			}
			else
			{
				return index*2;
			}
		}	
	}
	
	public int findSmallestGChild(int index)
	{
		if (index*2+1 > currentSize)					//similar thing done here
		{
			return index*2;
		}
		else
		{
			if (arr[index*2] > arr[index*2+1])
			{
				return index*2+1;
			}
			else
			{
				return index*2;
			}
		}

	}
	
	public int findSmallestOverall(int index1, int index2)
	{
		if (arr[index1] <= arr[index2])
		{
			return index1;
		}
		else
		{
			return index2;
		}
	}
	
	
	public void percolateDownMin(int index)
	{
		int smallestOverallIndex;
		boolean isGrandChild = false;
		if ((hasChildren(index) == true) && (hasGrandChildren(index*2) == true))
		{
			int smallestGChildIndex;
			int smallestGChildIndex1 = findSmallestGChild(index*2);
			int smallestGChildIndex2 = findSmallestGChild(index*2+1);
			
			if (smallestGChildIndex2 <= currentSize)													//if the index returned by the second gchild exists within the currentSize
			{
				smallestGChildIndex = findSmallestOverall(smallestGChildIndex1, smallestGChildIndex2);	//return the smallest between the two
			}
			else
			{
				smallestGChildIndex = smallestGChildIndex1;											//otherwise, the smallest gchild exists in the first child's kids
			}
			int smallestChildIndex = findSmallestChild(index);										//variable holding the smallest child
			smallestOverallIndex = findSmallestOverall(smallestChildIndex, smallestGChildIndex);	//found the smallest of the two, smallest overall 

			if (smallestOverallIndex == smallestGChildIndex)									//if the overall smallest is one of the gchildren, then set true
			{ 
				isGrandChild = true;
			}
			else
			{
				isGrandChild = false;
			}
		}
		else if ((hasChildren(index) == true) && (hasGrandChildren(index*2) == false))		//there are children no gchildren
		{
			smallestOverallIndex = findSmallestChild(index);
			isGrandChild = false;
		}
		else	//basically just hasChildren will be true
		{
			smallestOverallIndex = index;
			isGrandChild = false;
		}
				
		if (isGrandChild == true)							//if current index has grandchildren and the grandchild is the smallest
		{
			if (arr[smallestOverallIndex] < arr[index])   	 //basically a grandchild is being swapped around here
			{												//grandchildren should be greater than on a Min level
				
				swap(smallestOverallIndex, index);										//now the smallestoverallindex will be at the root, new min
				
				if (arr[smallestOverallIndex] > arr[parentIndex(smallestOverallIndex)])	//now if the original hole where the smallestoverallindex was contains an element greater than the parent (which its not supposed to)
				{
					swap(smallestOverallIndex, parentIndex(smallestOverallIndex));		//swap with the parent of the grandchild/smallestoverallindex
				}
				percolateDownMin(smallestOverallIndex);									//now it will be recursively checked again, testing the same hole that was originally the smallest
			}
		}
		else 	//if smallestOverallIndex belongs to a child of an index, so if isGrandChild = false
		{
			if (arr[smallestOverallIndex] < arr[index])
			{
				swap(smallestOverallIndex, index);
			}
		}
	}
	
	public int findLargestChild(int index)
	{
		if (index*2+1 > currentSize)
		{
			return index*2;
		}
		else
		{
			if (arr[index*2] < arr[index*2+1])
			{
				return index*2+1;
			}
			else
			{
				return index*2;
			}
		}
	}
	
	public int findLargestGChild(int index)
	{		
		if (index*2+1 > currentSize)
		{
			return index*2;
		}
		else
		{
			if (arr[index*2] < arr[index*2+1])
			{
				return index*2+1;
			}
			else
			{
				return index*2;
			}
		}
	}
	
	public int findLargestOverall(int index1, int index2)
	{
		if (arr[index1] >= arr[index2])
		{
			return index1;
		}
		else
		{
			return index2;
		}
	}
	
	public void percolateDownMax(int index)
	{
				int largestOverallIndex;
				boolean isGrandChild = false;
				if ((hasChildren(index) == true) && (hasGrandChildren(index*2) == true))
				{
					int largestGChildIndex;
					int largestGChildIndex1 = findLargestGChild(index*2);		//variable holding the largest grandchild
					int largestGChildIndex2 = findLargestGChild(index*2+1);
					
					if (largestGChildIndex2 <= currentSize)		//if gchildindex2 is returned, test to see if it can be used/is valid, to be set as the largestGChild
					{
						largestGChildIndex = findLargestOverall(largestGChildIndex1, largestGChildIndex2);
					}
					else
					{
						largestGChildIndex = largestGChildIndex1;
					}
					
					int largestChildIndex = findLargestChild(index);									//variable holding the largest child
					largestOverallIndex = findLargestOverall(largestChildIndex, largestGChildIndex);	//found the largest of the two, largest overall 
					if (largestOverallIndex == largestGChildIndex)	//if the overall largest is one of the gchildren, then set true
					{
						isGrandChild = true;
					}
					else
					{
						isGrandChild = false;
					}
				}
				else if ((hasChildren(index) == true) && (hasGrandChildren(index*2) == false))	//if only children exist, find the largest child
				{
					largestOverallIndex = findLargestChild(index);
					isGrandChild = false;
				}
				else																		//basically just hasChildren will be true
				{
					largestOverallIndex = index;											//the largest node is the index itself
					isGrandChild = false;
				}
						
				if (isGrandChild == true)		//if current index has grandchildren and the grandchild is the largest
				{
					if (arr[largestOverallIndex] > arr[index])    //basically a grandchild is being swapped around here
					{												//grandchildren should be smaller than on a max level
						
						swap(largestOverallIndex, index);	//now the largestoverallindex will be at the old max, becoming new
						
						if (arr[largestOverallIndex] < arr[parentIndex(largestOverallIndex)])	//now if the original hole where the largestoverallindex was contains an element less than the parent (which its not supposed to)
						{
							swap(largestOverallIndex, parentIndex(largestOverallIndex));	//swap with the parent of the grandchild/largestoverallIndex
						}
						percolateDownMax(largestOverallIndex);			//now it will be recursively checked again, testing the same hole that was originally the largest
					}
				}
				else 											//if largestOverallIndex belongs to a child of an index, so if isGrandChild = false
				{
					if (arr[largestOverallIndex] > arr[index])
					{
						swap(largestOverallIndex, index);
					}
				}	
	}
}
