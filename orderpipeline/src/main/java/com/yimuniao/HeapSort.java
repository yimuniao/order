package com.yimuniao;  
  
/** 
 * @author admin 
 * 
 */  
public class HeapSort  
{  
    /** *以2为底的对数 * 
     * @paramparam  
     * *@return  
     * */  
    private static double getLog(double param){   
        return Math.log(param)/Math.log(2);   
    }  
      
    private static void print(int[]data){    
        int pre=-2;    
        for(int i=0;i<data.length;i++){   
            if(pre<(int)getLog(i+1)){  
                pre=(int)getLog(i+1);   
//              System.out.println();   
            }   
            System.out.print(data[i]+ " ");   
        }   
    }  
      
    /** *排序，最大值放在末尾，data虽然是最大堆， 
     * 在排序后就成了递增的 *@paramdata  
     **/  
    private static void heapSort(int[]data){   
        //末尾与头交换，交换后调整最大堆   
        for(int i=data.length-1;i>0;i--){   
            int temp=data[0];   
            data[0]=data[i];   
            data[i]=temp;   
            maxHeapify(data,i,0);   
        }   
    }  
      
    /** *创建最大堆 * 
     * @paramdata  
     * *@paramheapSize 
     * 需要创建最大堆的大小，一般在sort的时候用到，因为最多值放在末尾，末尾就不再归入最大堆了  
     *@paramindex当前需要创建最大堆的位置  
    */  
    private static void maxHeapify(int[]data,int heapSize,int index){//4,1  
        //当前点与左右子节点比较   
        int left=getChildLeftIndex(index);//3  
        int right=getChildRightIndex(index);//4    
        int largest=index;//1  
        if(left<heapSize&&data[largest]<data[left]){   
            largest=left; //3  
        }   
        if(right<heapSize&&data[largest]<data[right]){   
            largest=right;   
        }   
//      System.out.println("index is : "+index +" left is : "+ left + " right is : "+right+" largest is : "+largest);  
        //得到最大值后可能需要交换，如果交换了，其子节点可能就不是最大堆了，需要重新调整   
        if(largest!=index){   
            int temp=data[index];   
            data[index]=data[largest];   
            data[largest]=temp;   
            maxHeapify(data,heapSize,largest);   
        }   
    }  
      
    private static int[] sort= new int[100001];  
    private static int j = 0 ;  
      
    static {  
        for(int i =0;i<=100000;i++){  
            sort[j]=i;  
            j++;  
        }  
    }  
   
    private static void buildMaxHeapify(int[]data){  
        //没有子节点的才需要创建最大堆，从最后一个的父节点开始   
        int startIndex=getParentIndex(data.length); //4  
        //从尾端开始创建最大堆，每次都是正确的堆  
        for(int i=startIndex;i>=0;i--){   
            maxHeapify(data,data.length,i); //4,1  
        }   
    }  
      
    /** *右子节点position  
     *@paramcurrent  
     **@return  
     **/  
    private static int getChildRightIndex(int current){   
        return(current<<1)+2;   
    }   
  
    /** *左子节点position注意括号，加法优先级更高 * 
     * @paramcurrent *@return  
     **/  
    private static int getChildLeftIndex(int current){   
        return(current<<1)+1;   
    }   
      
    /** *父节点位置 *@paramcurrent *@return */  
    private static int getParentIndex(int current){   
        return(current-2)>>1;   
    }  
      
    //插入排序  
    public static void insertion_sor(int[]data){   
        int j = 0;  
        int curData;  
        for(int i =1;i<data.length;i++){  
            curData = data[i];  
            j=i-1;  
            while((j>=0)&&curData<data[j]){  
                data[j+1]=data[j];  
                j--;  
            }  
            data[j+1]= curData;  
        }  
    }  
      
    public static void main(String[]args){   
        Long before = System.currentTimeMillis();  
//      insertion_sor(sort);  
        buildMaxHeapify(sort);   
        heapSort(sort);  
        Long after = System.currentTimeMillis();  
        System.out.println(after-before);  
//      print(sort);  
          
    }    
}  
      