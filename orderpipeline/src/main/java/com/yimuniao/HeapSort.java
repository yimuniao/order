package com.yimuniao;  
  
/** 
 * @author admin 
 * 
 */  
public class HeapSort  
{  
    /** *��2Ϊ�׵Ķ��� * 
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
      
    /** *�������ֵ����ĩβ��data��Ȼ�����ѣ� 
     * �������ͳ��˵����� *@paramdata  
     **/  
    private static void heapSort(int[]data){   
        //ĩβ��ͷ�������������������   
        for(int i=data.length-1;i>0;i--){   
            int temp=data[0];   
            data[0]=data[i];   
            data[i]=temp;   
            maxHeapify(data,i,0);   
        }   
    }  
      
    /** *�������� * 
     * @paramdata  
     * *@paramheapSize 
     * ��Ҫ�������ѵĴ�С��һ����sort��ʱ���õ�����Ϊ���ֵ����ĩβ��ĩβ�Ͳ��ٹ���������  
     *@paramindex��ǰ��Ҫ�������ѵ�λ��  
    */  
    private static void maxHeapify(int[]data,int heapSize,int index){//4,1  
        //��ǰ���������ӽڵ�Ƚ�   
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
        //�õ����ֵ�������Ҫ��������������ˣ����ӽڵ���ܾͲ��������ˣ���Ҫ���µ���   
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
        //û���ӽڵ�Ĳ���Ҫ�������ѣ������һ���ĸ��ڵ㿪ʼ   
        int startIndex=getParentIndex(data.length); //4  
        //��β�˿�ʼ�������ѣ�ÿ�ζ�����ȷ�Ķ�  
        for(int i=startIndex;i>=0;i--){   
            maxHeapify(data,data.length,i); //4,1  
        }   
    }  
      
    /** *���ӽڵ�position  
     *@paramcurrent  
     **@return  
     **/  
    private static int getChildRightIndex(int current){   
        return(current<<1)+2;   
    }   
  
    /** *���ӽڵ�positionע�����ţ��ӷ����ȼ����� * 
     * @paramcurrent *@return  
     **/  
    private static int getChildLeftIndex(int current){   
        return(current<<1)+1;   
    }   
      
    /** *���ڵ�λ�� *@paramcurrent *@return */  
    private static int getParentIndex(int current){   
        return(current-2)>>1;   
    }  
      
    //��������  
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
      